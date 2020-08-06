package com.varlamov.android.util.kotlin

inline fun <R> R?.orElse(block: () -> R): R {
    return this ?: block()
}