package com.eitanliu.overlay

import android.os.Build
import android.view.WindowManager
import com.eitanliu.overlay.OverlayWindow.Companion.PRIVATE_FLAG_NO_MOVE_ANIMATION


var WindowManager.LayoutParams.touchable
    get() = flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE == 0
    set(value) {
        if (value) flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
        else flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    }

var WindowManager.LayoutParams.outsideTouchable
    get() = flags and WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH == WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
    set(value) {
        if (value) flags or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        else flags and WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH.inv()
    }

var WindowManager.LayoutParams.moveAnimation
    get() = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            canPlayMoveAnimation()
        } else {
            rPrivateFlags and PRIVATE_FLAG_NO_MOVE_ANIMATION == 0
        }
    } catch (_: Throwable) {
        true
    }
    set(value) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                setCanPlayMoveAnimation(value)
            } else {
                updatePrivateFlags {
                    if (value) {
                        it and PRIVATE_FLAG_NO_MOVE_ANIMATION.inv()
                    } else {
                        it or PRIVATE_FLAG_NO_MOVE_ANIMATION
                    }
                }
            }
        } catch (_: Throwable) {
        }
    }

var WindowManager.LayoutParams.rPrivateFlags: Int
    get() {
        val field = WindowManager.LayoutParams::class.java.getField("privateFlags")
        field.isAccessible = true
        return field.getInt(this)
    }
    set(value) {
        val field = WindowManager.LayoutParams::class.java.getField("privateFlags")
        field.isAccessible = true
        field.setInt(this, value)
    }

fun WindowManager.LayoutParams.updatePrivateFlags(block: (flags: Int) -> Int) {
    val field = WindowManager.LayoutParams::class.java.getField("privateFlags")
    field.isAccessible = true
    field.setInt(this, block(field.getInt(this)))
}