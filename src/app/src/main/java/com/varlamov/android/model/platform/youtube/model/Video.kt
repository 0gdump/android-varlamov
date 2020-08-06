package com.varlamov.android.model.platform.youtube.model

data class Video(
    val kind: String,
    val etag: String,
    val id: VideoId,
    val snippet: VideoSnippet
)