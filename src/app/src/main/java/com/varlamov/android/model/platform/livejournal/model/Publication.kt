package com.varlamov.android.model.platform.livejournal.model

import org.joda.time.DateTime
import java.io.Serializable

data class Publication(
    val id: Int,
    val title: String,
    val content: String,
    val tags: List<String>,
    val time: DateTime,
    val url: String
) : Serializable