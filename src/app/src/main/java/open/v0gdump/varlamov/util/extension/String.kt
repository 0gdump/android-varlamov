package open.v0gdump.varlamov.util.extension

internal val REGEX_BASE_64 = Regex("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$")
internal val REGEX_VISUALS = Regex("[\\n\\t]")

fun String.isBase64(value: String): Boolean =
    value.matches(REGEX_BASE_64)

fun String.getWords(n: Int) =
    split(' ', limit = n)
        .subList(0, n - 1)
        .joinToString(" ")

fun String.optimize() =
    replace(REGEX_VISUALS, "")