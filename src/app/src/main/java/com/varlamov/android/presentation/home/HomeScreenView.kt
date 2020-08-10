package com.varlamov.android.presentation.home

import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.model.platform.youtube.model.Video
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface HomeScreenView : MvpView {

    fun showFreshPublication(publication: Publication)
    fun showErrorWhileLoadingFreshPublication()

    fun showLatestVideos(videos: List<Video>)
    fun showErrorWhileLoadingLatestVideos()

    fun showNews(news: List<Publication>)
    fun showErrorWhileLoadingNews()
}