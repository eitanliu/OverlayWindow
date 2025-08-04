package com.eitanliu.overlay

import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import java.lang.ref.Reference
import java.lang.ref.WeakReference

class OnSafeAreaChangeHandler(
    val handler: Handler = Handler(Looper.getMainLooper())
) : OnSafeAreaChangeListener {

    var delayMillis = 60L

    private var oldArea: Rect? = null
    private var newArea: Rect? = null
    private val listeners = mutableListOf<Reference<OnSafeAreaChangeListener>>()

    fun addListener(listener: OnSafeAreaChangeListener) {
        listeners.add(WeakReference(listener))
    }

    fun removeListener(listener: OnSafeAreaChangeListener) {
        listeners.removeAll { it.get() == listener }
    }

    override fun invoke(area: Rect) {
        updateSafeArea(area)
    }

    fun updateSafeArea(area: Rect, delay: Long = delayMillis) {
        newArea = area
        handler.removeCallbacks(updateRunnable)
        handler.postDelayed(updateRunnable, delay)
    }

    val updateRunnable = object : Runnable {

        override fun run() {
            val area = newArea
            if (area == null || oldArea == area) return
            oldArea = area
            for (reference in listeners) {
                val listener = reference.get()
                if (listener == null) {
                    listeners.remove(reference)
                } else {
                    listener(area)
                }
            }
        }
    }
}
