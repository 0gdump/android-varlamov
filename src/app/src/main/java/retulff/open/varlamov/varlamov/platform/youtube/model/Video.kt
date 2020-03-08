package retulff.open.varlamov.varlamov.platform.youtube.model

data class Video(
    val kind: String,
    val etag: String,
    val id: VideoId,
    val snippet: VideoSnippet
)