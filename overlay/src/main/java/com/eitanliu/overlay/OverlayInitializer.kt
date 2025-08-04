package com.eitanliu.overlay

import android.content.Context
import androidx.startup.Initializer
import java.lang.ref.WeakReference

class OverlayInitializer : Initializer<OverlayInitializer> {

    companion object {
        private var _context: WeakReference<Context?> = WeakReference(null)

        @JvmStatic
        val context: Context get() = _context.get() ?: throw OverlayNotInitializedException()

        @JvmStatic
        fun init(context: Context) {
            _context = WeakReference(context.applicationContext)
        }
    }

    override fun create(context: Context): OverlayInitializer {
        init(context)
        return this
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}