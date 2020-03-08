package retulff.open.varlamov.varlamov.platform.livejournal.model.factory

import android.util.Base64
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTimeZone
import retulff.open.varlamov.util.TimeUtils
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication

class PublicationsResponseFactoryManager {
    companion object {

        private val XML_RPC_START =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><methodResponse><params><param><value><struct>"

        fun convert(xml: String): List<Publication> {
            val publications = mutableListOf<Publication>()

            val initialOffset = xml.indexOf("struct", XML_RPC_START.length)
            var offset = initialOffset

            while (true) {

                val publication = parsePublication(xml, offset)

                publications += publication.first

                offset = publication.second

                // Отступ к новой публикации
                val offsetToNewStruct = xml.indexOf("<struct", offset)
                if (offsetToNewStruct == -1) {
                    break
                } else {
                    offset = offsetToNewStruct
                }
            }

            return publications
        }

        private fun parsePublication(xml: String, offset: Int): Pair<Publication, Int> {
            val titleKey = extractValue(xml, "subject", "base64", offset)
            val contentKey = extractValue(xml, "event", "base64", titleKey.second)
            val tagsKey = extractValue(xml, "taglist", "base64", contentKey.second)
            val timeKey = extractValue(xml, "logtime", "string", tagsKey.second)
            val urlKey = extractValue(xml, "url", "string", timeKey.second)

            val title = decodeBase64(titleKey.first)
            val content = decodeBase64(contentKey.first)
            val tags = ArrayList(decodeBase64(tagsKey.first).split(", "))
            val time = TimeUtils.parseString(
                timeKey.first,
                "yyyy-MM-dd HH:mm:ss",
                DateTimeZone.UTC
            )
            val url = urlKey.first

            return Pair(
                Publication(
                    title,
                    content,
                    tags,
                    time,
                    url
                ), urlKey.second
            )
        }

        private fun extractValue(xml: String, key: String, type: String, offset: Int): Key {

            val tail = "$key</name><value><$type>"

            val indexOfKey = xml.indexOf(key, offset)
            val indexOfBody = indexOfKey + tail.length
            val indexOfEnd = xml.indexOf('<', indexOfBody)

            return Pair(
                xml.substring(indexOfBody until indexOfEnd),
                indexOfEnd
            )
        }

        private fun decodeBase64(str: String): String {

            if (StringUtils.isNumeric(str)) return str

            return try {
                String(Base64.decode(str, Base64.NO_WRAP))
            } catch (ex: Exception) {
                str
            }
        }
    }
}