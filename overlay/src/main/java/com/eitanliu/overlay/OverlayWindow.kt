@file:Suppress("RtlHardcoded")

package com.eitanliu.overlay

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.annotation.CallSuper

open class OverlayWindow(
    open val view: View,
    open val windowManager: WindowManager = systemWindowManager
) {

    companion object {
        const val PRIVATE_FLAG_NO_MOVE_ANIMATION = 1 shl 6
        private const val AREA_VIEW_COLOR = Color.TRANSPARENT
        private const val AREA_VIEW_SIZE = 1

        val systemWindowManager get() = OverlayInitializer.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        private val safeAreaGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            safeAreaHandler.updateSafeArea(safeArea)
        }
        private var safeAreaInitialized = false
        private val safeAreaHandler by lazy { OnSafeAreaChangeHandler() }

        private val leftTop by lazy {
            OverlayWindow(View(OverlayInitializer.context)).apply {
                view.id = R.id.lt
                view.setBackgroundColor(AREA_VIEW_COLOR)
                layoutParams.apply {
                    width = AREA_VIEW_SIZE
                    height = AREA_VIEW_SIZE
                    flags = flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    gravity = Gravity.LEFT or Gravity.TOP
                }
                moveAnimation = false
            }
        }

        private val rightBottom by lazy {
            OverlayWindow(View(OverlayInitializer.context)).apply {
                view.id = R.id.rb
                view.setBackgroundColor(AREA_VIEW_COLOR)
                layoutParams.apply {
                    width = AREA_VIEW_SIZE
                    height = AREA_VIEW_SIZE
                    flags = flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    gravity = Gravity.RIGHT or Gravity.BOTTOM
                }
                moveAnimation = false
            }
        }

        val safeArea: Rect
            get() {
                val ltView = leftTop.view
                val rbView = rightBottom.view
                val ltRect = ltView.rectOnScreen
                val rbRect = rbView.rectOnScreen
                return Rect(
                    ltRect.left, ltRect.top,
                    rbRect.right, rbRect.bottom,
                )
            }

        fun addSafeAreaChangeListener(listener: OnSafeAreaChangeListener) {
            safeAreaHandler.addListener(listener)
        }

        fun removeSafeAreaChangeListener(listener: OnSafeAreaChangeListener) {
            safeAreaHandler.removeListener(listener)
        }

        var watchSafeArea = false
            set(value) {
                field = value
                updateSafeAreaAttached()
            }

        private fun updateSafeAreaAttached() {
            if (watchSafeArea) {
                safeAreaInitialized = true
                leftTop.attachView()
                rightBottom.attachView()
            } else {
                leftTop.detachView()
                rightBottom.detachView()
            }
        }
    }

    val context: Context get() = view.context

    var safeAreaChangeListener: OnSafeAreaChangeListener? = null

    open var layoutParams = WindowManager.LayoutParams().apply {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        gravity = Gravity.LEFT or Gravity.TOP

        type = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE
        } else {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                WindowManager.LayoutParams.FLAG_SPLIT_TOUCH or
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        format = PixelFormat.TRANSLUCENT
    }

    var touchable by layoutParams::touchable

    var outsideTouchable by layoutParams::outsideTouchable

    var moveAnimation by layoutParams::moveAnimation

    var visibility
        get() = view.visibility
        set(value) {
            view.visibility = value
        }

    fun toggleVisible(mask: Int = View.GONE) {
        view.visibility = if (view.visibility == View.VISIBLE) mask else View.VISIBLE
    }

    fun toggleAttached() {
        if (view.isAttachedToWindow) {
            detachView()
        } else {
            attachView()
        }
    }

    @CallSuper
    open fun attachView() {
        if (view.isAttachedToWindow) return
        try {
            windowManager.addView(
                view, layoutParams
            )
            if (watchSafeArea) {
                if (this === leftTop || this === rightBottom) {
                    view.viewTreeObserver.addOnGlobalLayoutListener(safeAreaGlobalLayoutListener)
                }
            }
        } catch (e: Throwable) {
        }
    }

    @CallSuper
    open fun detachView() {
        if (!view.isAttachedToWindow) return

        if (watchSafeArea.not() && safeAreaInitialized) {
            if (this === leftTop || this === rightBottom) {
                view.viewTreeObserver.removeOnGlobalLayoutListener(
                    safeAreaGlobalLayoutListener
                )
            }
        }
        windowManager.removeView(view)
    }

    fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit) {
        block(layoutParams)
        updateLayoutParams(layoutParams)
    }

    fun updateLayoutParams(layoutParams: WindowManager.LayoutParams = this.layoutParams) {
        try {
            if (layoutParams != this.layoutParams) this.layoutParams = layoutParams
            if (view.isAttachedToWindow) {
                windowManager.updateViewLayout(view, layoutParams)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setOnSafeAreaChangeListener(listener: OnSafeAreaChangeListener?) {
        val oldListener = safeAreaChangeListener
        safeAreaChangeListener = listener
        if (listener != null) {
            if (watchSafeArea.not()) watchSafeArea = true
            addSafeAreaChangeListener(listener)
        } else if (oldListener != null) {
            removeSafeAreaChangeListener(oldListener)
        }
    }
}
