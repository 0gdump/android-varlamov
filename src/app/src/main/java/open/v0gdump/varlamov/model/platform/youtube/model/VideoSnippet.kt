package open.v0gdump.varlamov.model.platform.youtube.model

data class VideoSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: VideoThumbnails
)