package open.v0gdump.varlamov.util

inline fun <R> R?.orElse(block: () -> R): R {
    return this ?: block()
}