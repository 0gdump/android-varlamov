package open.v0gdump.varlamov.model.platform.youtube.model

data class VideosRequestResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: RequestInfo,
    val items: List<Video>
)