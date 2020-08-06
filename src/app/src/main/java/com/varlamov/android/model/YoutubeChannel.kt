package com.varlamov.android.model

import com.varlamov.android.BuildConfig
import com.varlamov.android.model.platform.youtube.model.VideosRequestResponse
import com.varlamov.android.model.platform.youtube.service.YoutubeService
import retrofit2.Callback

object YoutubeChannel {

    private val youtubeService = YoutubeService.create()

    fun getLatestVideos(count: Int, callback: Callback<VideosRequestResponse>) =
        youtubeService.getLatestVideo(
            BuildConfig.YOUTUBE_KEY,
            "UC101o-vQ2iOj9vr00JUlyKw",
            count
        ).enqueue(callback)
}