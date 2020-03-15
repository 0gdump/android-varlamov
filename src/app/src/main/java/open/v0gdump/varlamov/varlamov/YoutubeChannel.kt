package open.v0gdump.varlamov.varlamov

import open.v0gdump.varlamov.varlamov.platform.youtube.model.VideosRequestResponse
import open.v0gdump.varlamov.varlamov.platform.youtube.service.YoutubeService
import retrofit2.Callback

object YoutubeChannel {

    // FIXME Ключи были деактивированы. Брать их из env
    private const val AUTH_KEY = "AIzaSyCLoo9-Im-a2juKcBBwNxgRXh7ZM8RyDBw"
    private const val CHANNEL_ID = "UC101o-vQ2iOj9vr00JUlyKw"

    private val youtubeService = YoutubeService.create()

    fun getLatestVideos(count: Int, callback: Callback<VideosRequestResponse>) =
        youtubeService.getLatestVideo(AUTH_KEY, CHANNEL_ID, count).enqueue(callback)
}