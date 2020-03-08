package retulff.open.varlamov.varlamov.platform.livejournal.model.factory
/*
import android.util.Base64
import org.apache.commons.lang3.StringUtils.isNumeric
import org.joda.time.DateTimeZone
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import retulff.open.varlamov.util.StringUtils.Companion.isBase64
import retulff.open.varlamov.util.TimeUtils
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.platform.livejournal.model.PublicationsResponse
import java.io.StringReader

class PublicationsResponseFactoryXPP {
    companion object {

        fun convert(xml: String): PublicationsResponse {

            //return PublicationsResponseFactory.convert(
            //    Jsoup.parse(xml, "", Parser.xmlParser())
            //)

            val factory = XmlPullParserFactory.newInstance().apply {
                isNamespaceAware = false
                //isValidating = false
            }
            val xpp = factory.newPullParser()

            xpp.setInput(StringReader(xml))

            var skip = -1
            var publicationsRaw = listOf<HashMap<String, String>>()
            var lastSyncRaw = ""

            val eventType = xpp.eventType
            parser@ while (eventType != XmlPullParser.END_DOCUMENT) {
                when (xpp.eventType) {

                    XmlPullParser.START_TAG -> {
                        if (xpp.name != "methodResponse" &&
                            xpp.name != "params" &&
                            xpp.name != "param" &&
                            xpp.name != "value" &&
                            xpp.name != "struct"
                        ) {
                            when {
                                skip == -1 -> {
                                    memberName(xpp)
                                    skip = memberParseInt(xpp)
                                }
                                publicationsRaw.isEmpty() -> {
                                    memberName(xpp)
                                    publicationsRaw = memberParseArray(xpp)
                                }
                                lastSyncRaw.isEmpty() -> {
                                    memberName(xpp)
                                    lastSyncRaw = memberParseSimpleValue(xpp)
                                }
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (xpp.name == "methodResponse") {
                            break@parser
                        }
                    }

                    XmlPullParser.START_DOCUMENT,
                    XmlPullParser.TEXT -> {
                        // Pass
                    }

                    else -> {
                        // Unreal
                    }
                }

                xpp.next()
            }

            val lastSync = TimeUtils.parseString(
                lastSyncRaw,
                "yyyy-MM-dd HH:mm:ss",
                DateTimeZone.UTC
            )

            val publications = mutableListOf<Publication>()
            publicationsRaw.forEach {
                publications += Publication(
                    decodeString(it["subject"]!!),
                    decodeString(it["event"]!!),
                    ArrayList(listOf("tag1", "tag2")),
                    TimeUtils.parseString(
                        it["logtime"]!!,
                        "yyyy-MM-dd HH:mm:ss",
                        DateTimeZone.UTC
                    ),
                    it["url"]!!
                )
            }

            return PublicationsResponse(skip, publications, lastSync)
        }

        private fun memberName(xpp: XmlPullParser): String {
            xpp.nextTag()  // <name>
            xpp.next()     // text

            val memberName: String = if (xpp.text != null) xpp.text else ""

            xpp.nextTag()  // </name>

            return memberName
        }

        private fun memberParseSimpleValue(xpp: XmlPullParser): String {
            xpp.nextTag()  // <value>
            xpp.nextTag()  // <...>

            xpp.next()
            val content =
                if (xpp.text != null)
                    xpp.text
                else
                    ""

            // Find not simple value
            if (content.isEmpty()) {
                while (true) {
                    if (xpp.name == "struct" && xpp.eventType == XmlPullParser.END_TAG) {
                        break
                    }

                    xpp.next()
                }
            } else {
                xpp.nextTag()  // </...>
            }

            xpp.nextTag()  // </value>

            return content
        }

        private fun memberParseInt(xpp: XmlPullParser): Int {
            return memberParseSimpleValue(xpp).toInt()
        }

        private fun parseMember(
            xpp: XmlPullParser,
            onMember: Boolean = false
        ): Pair<String, String> {
            if (!onMember) {
                xpp.nextTag() // <member>
            }

            val name = memberName(xpp)
            val value = memberParseSimpleValue(xpp)

            xpp.nextTag()  // </member>

            return Pair(name, value)
        }

        private fun memberParseArray(xpp: XmlPullParser): MutableList<HashMap<String, String>> {
            xpp.nextTag()  // <value>
            xpp.nextTag()  // <array>
            xpp.nextTag()  // <data>
            xpp.nextTag()  // <value>

            val array = mutableListOf<HashMap<String, String>>()
            val struct = HashMap<String, String>()

            while (true) {
                xpp.nextTag()

                if (xpp.name == "member") {
                    struct += parseMember(xpp, true)
                } else if (xpp.name == "struct" && xpp.eventType == XmlPullParser.START_TAG) {
                    struct.clear()
                } else if (xpp.name == "struct" && xpp.eventType == XmlPullParser.END_TAG) {
                    array.add(struct)

                    xpp.next()  // </value>
                    xpp.next()  // </data>
                } else {
                    break
                }
            }

            return array
        }

        private fun decodeString(str: String): String {

            if (isNumeric(str)) return str
            if (!isBase64(str)) return str

            return try {
                String(Base64.decode(str, Base64.NO_WRAP))
            } catch (ex: Exception) {
                str
            }
        }
    }
}*/