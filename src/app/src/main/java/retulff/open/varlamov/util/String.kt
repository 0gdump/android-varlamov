package retulff.open.varlamov.util

fun String.isBase64(value: String): Boolean =
    value.matches(Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$"))

fun String.getWords(n: Int) =
    split(' ', limit = n)
        .subList(0, n - 1)
        .joinToString(" ")

fun String.optimize() =
    replace(Regex("[\\n\\t]"), "")