package open.v0gdump.varlamov.varlamov.platform.livejournal.model

import android.util.Base64
import open.v0gdump.varlamov.util.DateTimeUtils
import org.joda.time.DateTimeZone

// TODO: Доработать с использованием корутин
class PublicationsFactory {

    private data class ParsedKey(
        val value: String,
        val offsetToEnd: Int
    )

    private data class ParsedPublication(
        val publication: Publication,
        val offsetToEndOfLastKey: Int
    )

    companion object {

        // Начало XML RPC ответа
        private const val XML_RPC_START_LEN =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><methodResponse><params><param><value><struct>"
                .length

        fun convert(xml: String): List<Publication> {

            /*
             *   Для производительности используется последовательный поиск по строке
             * необходимых ключей и вычленение значений.
             * Этот алгоритм очень строго завязан на структуре ответа от сервера
             * и очень ненадёжен. Но за то даёт огромный прирост в скорости.
             *
             * Скорость парсинга ответа на 20 публикаций:
             * JSoup  - 1400 мс
             * XPP    - 700 мс
             * Search - 300 мс
             *
             * Так же, данный метод очень легко распараллеливается.
             */

            val publications = mutableListOf<Publication>()

            var offset = XML_RPC_START_LEN
            while (true) {

                offset = xml.indexOf("<struct", offset)
                if (offset == -1) {
                    break
                }

                val parsedPublication = parsePublication(xml, offset)

                publications += parsedPublication.publication
                offset = parsedPublication.offsetToEndOfLastKey
            }

            return publications
        }

        private fun parsePublication(
            xml: String,
            initialOffset: Int
        ): ParsedPublication {

            // Важно соблюдать последовательность поиска ключей!
            val titleKey = parseKey(xml, "subject", "base64", initialOffset)
            val contentKey = parseKey(xml, "event", "base64", titleKey.offsetToEnd)
            val tagsKey = parseKey(xml, "taglist", "base64", contentKey.offsetToEnd)
            val timeKey = parseKey(xml, "logtime", "string", tagsKey.offsetToEnd)
            val urlKey = parseKey(xml, "url", "string", timeKey.offsetToEnd)

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
             * XML представление значения
             *
             * <member>
             *   <name> NAME </name
             *   <value>
             *     <TYPE> VALUE </TYPE>
             *   </value>
             * </member>
             */


            val indexOfKey = xml.indexOf(key, offset)

            val tail = "$key</name><value><$type>"
            val indexOfBody = indexOfKey + tail.length

            val indexOfEnd = xml.indexOf('<', indexOfBody)

            return ParsedKey(
                xml.substring(indexOfBody until indexOfEnd),
                indexOfEnd
            )
        }

        private fun decodeBase64(str: String): String =
            try {
                String(Base64.decode(str, Base64.NO_WRAP))
            } catch (ex: Exception) {
                str
            }
    }
}