package com.varlamov.android.presentation.home

import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.model.Blog
import com.varlamov.android.model.YoutubeChannel
import com.varlamov.android.model.platform.livejournal.model.PublicationsFactory
import com.varlamov.android.model.platform.youtube.model.VideosRequestResponse
import moxy.InjectViewState
import moxy.MvpPresenter
import okhttp3.ResponseBody
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InjectViewState
class HomeScreenPresenter : MvpPresenter<HomeScreenView>() {

    companion object {
        const val NEWS_COUNT = 5
        const val VIDEOS_COUNT = 5
    }

    fun loadFreshPublication() {

        val utcNow = DateTime.now().withZone(DateTimeZone.UTC)
        val callback = object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, e: Throwable) {
                viewState.showErrorWhileLoadingFreshPublication()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) {
                    viewState.showErrorWhileLoadingFreshPublication()
                    return
                }

                val responseBody = response.body()!!.string()
                val posts = PublicationsFactory.convert(responseBody)

                if (posts.isEmpty()) {
                    viewState.showErrorWhileLoadingFreshPublication()
                    return
                }

                viewState.showFreshPublication(posts.first())
            }
        }

        Blog.getPublications(1, utcNow, callback)
    }

    fun loadLatestVideos() = YoutubeChannel.getLatestVideos(
        VIDEOS_COUNT,
        object : Callback<VideosRequestResponse> {

            override fun onFailure(call: Call<VideosRequestResponse>, t: Throwable) =
                viewState.showErrorWhileLoadingLatestVideos()

            override fun onResponse(
                call: Call<VideosRequestResponse>,
                response: Response<VideosRequestResponse>
            ) {
                if (!response.isSuccessful || response.body() == null) {
                    viewState.showErrorWhileLoadingLatestVideos()
                    return
                }

                viewState.showLatestVideos(response.body()!!.items)
            }
        }
    )

    fun loadNews() {

        val utcNow = DateTime.now().withZone(DateTimeZone.UTC)
        val callback = object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, e: Throwable) =
                viewState.showErrorWhileLoadingNews()

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) {
                    viewState.showErrorWhileLoadingNews()
                    return
                }

                val responseBody = response.body()!!.string()
                val posts = PublicationsFactory.convert(responseBody)

                if (posts.isEmpty()) {
                    viewState.showErrorWhileLoadingNews()
                    return
                }

                viewState.showNews(posts)
            }
        }

        Blog.getPublicationsByTag(
            App.res.getString(R.string.news_tag),
            NEWS_COUNT,
            utcNow,
            callback
        )
    }
}