package retulff.open.varlamov.util

class StringUtils {
    companion object {

        private val BASE64_CHARS = charArrayOf(
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            '+',
            '/',
            '='
        )

        fun isBase64(value: String): Boolean {

            value.toLowerCase().forEach {
                if (!BASE64_CHARS.contains(it)) return false
            }

            return true
        }

        fun getNWordsFrom(value: String, n: Int): String {
            return value
                .split(" ", limit = n)
                .subList(0, n - 1)
                .joinToString(" ")
        }
    }
}