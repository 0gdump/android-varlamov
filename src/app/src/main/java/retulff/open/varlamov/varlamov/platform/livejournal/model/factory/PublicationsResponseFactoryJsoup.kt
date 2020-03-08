package retulff.open.varlamov.varlamov.platform.livejournal.model.factory
/*
import android.util.Base64
import org.apache.commons.lang3.StringUtils.isNumeric
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retulff.open.varlamov.util.StringUtils.Companion.isBase64
import retulff.open.varlamov.util.TimeUtils
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.platform.livejournal.model.PublicationsResponse

class PublicationsResponseFactoryJsoup {
    companion object {

        fun convert(xmlDoc: Document): PublicationsResponse {

            val skip = convertSkipNode(xmlDoc)
            val items = convertItems(xmlDoc)
            val lastSync = convertUTCLastSync(xmlDoc)

            return PublicationsResponse(skip, items, lastSync)
        }

        private fun convertSkipNode(xmlDoc: Document): Int {

            val node = getMemberByName(xmlDoc, "skip")
            val value = getValueFromMember(node)

            return value.toInt()
        }

        private fun convertItems(xmlDoc: Document): ArrayList<Publication> {

            val node = getMemberByName(xmlDoc, "events")
            val items = node.select("data > value")

            val publications = ArrayList<Publication>()

            items.forEach {
                try {

                    // Find nodes
                    val nodeCaption = getMemberByName(it, "subject")
                    val nodeText = getMemberByName(it, "event")
                    val nodeTagList = getMemberByName(it, "taglist")
                    val nodeUtcLogTime = getMemberByName(it, "logtime")
                    val nodeUrl = getMemberByName(it, "url")

                    // Get content
                    val caption = decodeMember(nodeCaption)
                    val content = decodeMember(nodeText)
                    val tagList = ArrayList(decodeMember(nodeTagList).split(", "))
                    val utcLogTime = TimeUtils.parseString(
                        getValueFromMember(nodeUtcLogTime),
                        "yyyy-MM-dd HH:mm:ss",
                        DateTimeZone.UTC
                    )
                    val url = getValueFromMember(nodeUrl)

                    // Add to collection
                    publications.add(
                        Publication(
                            caption,
                            content,
                            tagList,
                            utcLogTime,
                            url
                        )
                    )
                } catch (e: Exception) {
                }
            }

            return publications
        }

        private fun convertUTCLastSync(xmlDoc: Document): DateTime {

            val node = getMemberByName(xmlDoc, "lastsync")

            return TimeUtils.parseString(
                getValueFromMember(node),
                "yyyy-MM-dd HH:mm:ss",
                DateTimeZone.UTC
            )
        }

        fun getMemberByName(root: Element, name: String): Element {
            return root.selectFirst("name:contains($name)").parent()
        }

        private fun getValueFromMember(element: Element): String {
            return element.selectFirst("value").text()
        }

        private fun decodeMember(element: Element): String {

            val value = getValueFromMember(element)

            if (isNumeric(value)) return value
            if (!isBase64(value)) return value

            return try {
                String(Base64.decode(value, Base64.NO_WRAP))
            } catch (ex: Exception) {
                value
            }
        }
    }
}*/