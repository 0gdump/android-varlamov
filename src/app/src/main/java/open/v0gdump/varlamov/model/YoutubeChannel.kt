package open.v0gdump.varlamov.model

import open.v0gdump.varlamov.model.platform.youtube.model.VideosRequestResponse
import open.v0gdump.varlamov.model.platform.youtube.service.YoutubeService
import retrofit2.Callback

object YoutubeChannel {

    // FIXME Ключи были деактивированы. Брать их из env
    private const val AUTH_KEY = "-"
    private const val CHANNEL_ID = "UC101o-vQ2iOj9vr00JUlyKw"

    private val youtubeService = YoutubeService.create()

    fun getLatestVideos(count: Int, callback: Callback<VideosRequestResponse>) =
        youtubeService.getLatestVideo(AUTH_KEY, CHANNEL_ID, count).enqueue(callback)
}