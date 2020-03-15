package open.v0gdump.varlamov.ui.fragment

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication
import open.v0gdump.varlamov.varlamov.platform.youtube.model.Video

@StateStrategyType(OneExecutionStateStrategy::class)
interface HomeView : MvpView {

    fun showFreshPublication(publication: Publication)
    fun showErrorWhileLoadingFreshPublication()

    fun showLatestVideos(videos: List<Video>)
    fun showErrorWhileLoadingLatestVideos()

    fun showNews(news: List<Publication>)
    fun showErrorWhileLoadingNews()
}