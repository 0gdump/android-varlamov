package open.v0gdump.varlamov.presentation.home

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.platform.youtube.model.Video

@StateStrategyType(AddToEndSingleStrategy::class)
interface HomeScreenView : MvpView {

    fun showFreshPublication(publication: Publication)
    fun showErrorWhileLoadingFreshPublication()

    fun showLatestVideos(videos: List<Video>)
    fun showErrorWhileLoadingLatestVideos()

    fun showNews(news: List<Publication>)
    fun showErrorWhileLoadingNews()
}