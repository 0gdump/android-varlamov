package retulff.open.varlamov.varlamov

import retrofit2.Callback
import retulff.open.varlamov.varlamov.platform.youtube.model.VideosRequestResponse
import retulff.open.varlamov.varlamov.platform.youtube.service.YoutubeService

class YoutubeChannel {
    companion object {

        private const val AUTH_KEY = "AIzaSyCLoo9-Im-a2juKcBBwNxgRXh7ZM8RyDBw"
        private const val CHANNEL_ID = "UC101o-vQ2iOj9vr00JUlyKw"

        private var youtubeService = YoutubeService.create()

        fun getLatestVideos(count: Int, callback: Callback<VideosRequestResponse>) {

            youtubeService.getLatestVideo(AUTH_KEY, CHANNEL_ID, count).enqueue(callback)
        }
    }
}