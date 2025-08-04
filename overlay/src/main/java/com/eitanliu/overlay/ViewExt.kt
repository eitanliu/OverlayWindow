package com.eitanliu.overlay

import android.graphics.Point
import android.graphics.Rect
import android.view.View


inline fun <reified T> View.parentIf(): T? {
    var view = parent
    while (view != null) {
        if (view is T) {
            return view
        } else {
            view = view.parent
        }
    }
    return null
}

/**
 * View可见部分 相对于 自身View位置左上角 的坐标
 */
inline val View.visibleOnSelf get() = Rect().also { getLocalVisibleRect(it) }

/**
 * View可见部分 相对于 父View 的坐标
 */
inline val View.visibleOnParent
    get() = Rect().also {
        getLocalVisibleRect(it)
        val point = pointOnParent
        pointOnScreen
        it.offset(point.x, point.y)
    }

/**
 * View可见部分 相对于 屏幕 的坐标
 */
inline val View.visibleOnScreen
    get() = Rect().also {
        getGlobalVisibleRect(it)
        val point = rootView.pointOnScreen
        it.offset(point.x, point.y)
    }

/**
 * 获得 View 相对 父View 的坐标
 */
inline val View.rectOnSelf
    get() = Rect().also {
        it.left = 0
        it.top = 0
        it.right = width
        it.bottom = height
    }

/**
 * 获得 View 相对 父View 的坐标
 */
inline val View.rectOnParent
    get() = Rect().also {
        val location = locationOnParent
        it.left = location[0]
        it.top = location[1]
        it.right = it.left + width
        it.bottom = it.top + height
    }

/**
 * 获取控件 相对 窗口Window 的坐标
 */
inline val View.rectOnWindow
    get() = Rect().also {
        val location = locationOnWindow
        it.left = location[0]
        it.top = location[1]
        it.right = it.left + width
        it.bottom = it.top + height
    }

/**
 * 获得 View 相对 屏幕 的绝对坐标
 */
inline val View.rectOnScreen
    get() = Rect().also {
        val location = locationOnScreen
        it.left = location[0]
        it.top = location[1]
        it.right = it.left + width
        it.bottom = it.top + height
    }

inline val View.pointOnParent get() = locationOnParent.let { Point(it[0], it[1]) }

inline val View.pointOnWindow get() = locationOnWindow.let { Point(it[0], it[1]) }

inline val View.pointOnScreen get() = locationOnScreen.let { Point(it[0], it[1]) }

/**
 * 获得 View 相对 父View 的坐标
 */
val View.locationOnParent: IntArray
    get() {
        val location = IntArray(2)
        getLocationInWindow(location)
        (parent as? View)?.also { parent ->
            val parentLocation = IntArray(2)
            parent.getLocationInWindow(parentLocation)
            location[0] -= parentLocation[0]
            location[1] -= parentLocation[1]
        }
        return location
    }

/**
 * 获取控件 相对 窗口Window 的坐标
 */
val View.locationOnWindow get() = IntArray(2).also { getLocationInWindow(it) }

/**
 * 获得 View 相对 屏幕 的绝对坐标
 */
val View.locationOnScreen get() = IntArray(2).also { getLocationOnScreen(it) }