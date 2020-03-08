package retulff.open.varlamov.varlamov.platform.youtube.model

data class VideoSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: VideoThumbnails
)