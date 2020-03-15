package open.v0gdump.varlamov.util.extension

inline fun <R> R?.orElse(block: () -> R): R {
    return this ?: block()
}