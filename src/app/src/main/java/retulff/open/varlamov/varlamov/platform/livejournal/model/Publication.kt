package retulff.open.varlamov.varlamov.platform.livejournal.model

import org.joda.time.DateTime

data class Publication(
    val title: String,
    val content: String,
    val tagList: ArrayList<String>,
    val utcLogTime: DateTime,
    val url: String
)