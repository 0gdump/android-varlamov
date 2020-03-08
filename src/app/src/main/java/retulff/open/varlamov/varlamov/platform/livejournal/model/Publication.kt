package retulff.open.varlamov.varlamov.platform.livejournal.model

import org.joda.time.DateTime

data class Publication(
    val title: String,
    val content: String,
    val tags: List<String>,
    val time: DateTime,
    val url: String
)