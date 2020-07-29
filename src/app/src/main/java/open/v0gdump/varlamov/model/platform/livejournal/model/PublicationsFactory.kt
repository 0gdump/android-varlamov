package open.v0gdump.varlamov.model.platform.livejournal.model

import android.util.Base64
import open.v0gdump.varlamov.util.DateTimeUtils
import org.joda.time.DateTimeZone

// TODO(CODE) Use new json api
object PublicationsFactory {

    private data class ParsedKey(
        val value: String,
        val offsetToEnd: Int
    )

    private data class ParsedPublication(
        val publication: Publication,
        val offsetToEndOfLastKey: Int
    )

    private const val XML_RPC_HEADER =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><methodResponse><params><param><value><struct>"
    private const val XML_RPC_HEADER_LEN = XML_RPC_HEADER.length

    fun convert(xml: String): List<Publication> {

        /*
         * Для поиска ключей и значений используется линейный поиск по строке.
         * Строгая привязка к структуре ответа делает алгоритм ненадёжным.
         * Эта реализация оставлена в угоду производительности.
         * Так же её можно распараллелить
         *
         * Сравнение алгоритмов, конкретные значения времени получены на SDM660:
         * Current | x, ~300 мс
         * XPP     | 2x ~700 мс
         * Soup    | 5x, ~1400 мс
         *
         */

        val publications = mutableListOf<Publication>()

        var offset = XML_RPC_HEADER_LEN
        while (true) {
            offset = xml.indexOf("<struct", offset)
            if (offset == -1) {
                break
            }

            parsePublication(xml, offset).let {
                publications += it.publication
                offset = it.offsetToEndOfLastKey
            }
        }

        return publications
    }

    private fun parsePublication(
        xml: String,
        initialOffset: Int
    ): ParsedPublication {

        // Порядок ключей критичен!
        val idKey = parseKey(xml, "itemid", "int", initialOffset)
        val titleKey = parseKey(xml, "subject", "base64", idKey.offsetToEnd)
        val contentKey = parseKey(xml, "event", "base64", titleKey.offsetToEnd)
        val tagsKey = parseKey(xml, "taglist", "base64", contentKey.offsetToEnd)
        val timeKey = parseKey(xml, "logtime", "string", tagsKey.offsetToEnd)
        val urlKey = parseKey(xml, "url", "string", timeKey.offsetToEnd)

        val id = idKey.value.toInt()
        val title = decodeBase64(titleKey.value)
        val content = decodeBase64(contentKey.value)
        val tags = decodeBase64(tagsKey.value).split(", ")
        val time = DateTimeUtils.parseString(
            timeKey.value,
            "yyyy-MM-dd HH:mm:ss",
            DateTimeZone.UTC
        )
        val url = urlKey.value

        return ParsedPublication(
            Publication(
                id,
                title,
                content,
                tags,
                time,
                url
            ),
            urlKey.offsetToEnd
        )
    }

    private fun parseKey(
        xml: String,
        key: String,
        type: String,
        offset: Int
    ): ParsedKey {

        /*
         * Алгоритм разбора:
         *  - Ищём ключ в xml
         *  - Рассчитываем отступ до значения
         *  - Ищём конец данных
         *  - Возвращаем строку между скобками > <
         *
         * Значение в XML RPC представляется так:
         *
         * <member>
         *   <name>NAME</name>
         *   <value>
         *     <TYPE>VALUE</TYPE>
         *   </value>
         * </member>
         */

        val indexOfKey = xml.indexOf(key, offset)
        val keyTail = "$key</name><value><$type>"

        val indexOfBody = indexOfKey + keyTail.length
        val indexOfEnd = xml.indexOf('<', indexOfBody)

        return ParsedKey(
            xml.substring(indexOfBody until indexOfEnd),
            indexOfEnd
        )
    }

    private fun decodeBase64(str: String): String {
        return try {
            String(Base64.decode(str, Base64.NO_WRAP))
        } catch (ex: Exception) {
            str
        }
    }
}