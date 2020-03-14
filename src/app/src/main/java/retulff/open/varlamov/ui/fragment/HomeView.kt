package retulff.open.varlamov.ui.fragment

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.platform.youtube.model.Video

@StateStrategyType(OneExecutionStateStrategy::class)
interface HomeView : MvpView {

    fun showFreshPublication(publication: Publication)
    fun showErrorWhileLoadingFreshPublication()

    fun showLatestVideos(videos: List<Video>)
    fun showErrorWhileLoadingLatestVideos()

    fun showNews(news: List<Publication>)
    fun showErrorWhileLoadingNews()
}