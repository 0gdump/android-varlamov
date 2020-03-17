package open.v0gdump.varlamov.extension

inline fun <R> R?.orElse(block: () -> R): R {
    return this ?: block()
}