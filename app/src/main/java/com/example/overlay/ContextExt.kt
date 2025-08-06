package com.example.overlay

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import kotlin.apply
import kotlin.jvm.java

fun Context.contextTree(tree: (context: Context) -> Unit) {
    tree(this)
    if (this is ContextWrapper) baseContext.contextTree(tree)
}

inline fun <reified T> Context.typeIf(): T? {
    return contextIf { it is T } as? T
}

fun Context.contextIf(predicate: (context: Context) -> Boolean): Context? {
    var current: Context? = this
    while (current != null) {
        if (predicate(current)) {
            return current
        } else if (current is ContextWrapper) {
            val base = current.baseContext
            current = base
        } else {
            current = null
        }
    }
    return null
}

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(intent<T>())
}

inline fun <reified T : Activity> Context.startActivity(
    noinline action: Intent.() -> Unit
) {
    startActivity(intent<T>().apply(action))
}

inline fun <reified T : Activity> Context.intent(): Intent {
    return Intent(this, T::class.java)
}