package com.example.overlay

import android.view.View
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


val Window.isShowSoftwareKeyboard
    get() = decorView.rootWindowInsetsCompat?.isShowSoftwareKeyboard == true

inline val Window.systemBarsInsets
    get() = decorView.rootWindowInsetsCompat?.systemBarsInsets

inline val Window.imeSystemBarsInsets
    get() = decorView.rootWindowInsetsCompat?.imeSystemBarsInsets

inline val Window.statusBarsInsets
    get() = decorView.rootWindowInsetsCompat?.statusBarsInsets

inline val Window.navigationBarsInsets
    get() = decorView.rootWindowInsetsCompat?.navigationBarsInsets

val Window.rootWindowInsetsCompat
    get() = ViewCompat.getRootWindowInsets(decorView)

val View.isShowSoftwareKeyboard
    get() = rootWindowInsetsCompat?.isShowSoftwareKeyboard == true

inline val View.systemBarsInsets
    get() = rootWindowInsetsCompat?.systemBarsInsets

inline val View.imeSystemBarsInsets
    get() = rootWindowInsetsCompat?.imeSystemBarsInsets

inline val View.statusBarsInsets
    get() = rootWindowInsetsCompat?.statusBarsInsets

inline val View.navigationBarsInsets
    get() = rootWindowInsetsCompat?.navigationBarsInsets

inline val View.imeInsets
    get() = rootWindowInsetsCompat?.imeInsets

val View.rootWindowInsetsCompat
    get() = ViewCompat.getRootWindowInsets(this)

val WindowInsetsCompat.isShowSoftwareKeyboard: Boolean
    get() {
        val imeBottom = imeInsets.bottom
        val navBottom = navigationBarsInsets.bottom
        return imeBottom > navBottom
    }

inline val WindowInsetsCompat.systemBarsInsets
    get() = getInsets(WindowInsetsCompat.Type.systemBars())

inline val WindowInsetsCompat.imeSystemBarsInsets
    get() = getInsets(WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.systemBars())

inline val WindowInsetsCompat.statusBarsInsets
    get() = getInsets(WindowInsetsCompat.Type.statusBars())

inline val WindowInsetsCompat.navigationBarsInsets
    get() = getInsets(WindowInsetsCompat.Type.navigationBars())

inline val WindowInsetsCompat.imeInsets
    get() = getInsets(WindowInsetsCompat.Type.ime())

