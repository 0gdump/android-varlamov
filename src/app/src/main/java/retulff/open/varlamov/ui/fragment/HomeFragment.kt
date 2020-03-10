package retulff.open.varlamov.ui.fragment

import android.util.Log
import kotlinx.android.synthetic.main.fragment_home.view.*
import moxy.ktx.moxyPresenter
import retulff.open.varlamov.R
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.platform.youtube.model.Video

class HomeFragment : MvpFragmentX(R.layout.fragment_home), HomeView {

    private val presenter by moxyPresenter { HomePresenterImpl() }

    override fun setupLayout() {

        // TODO Создать toolbar и настроить

        setupSwipeRefresh()
        setupCards()

        fetchData()
    }

    private fun setupSwipeRefresh() {
        layout.home_refresh.setOnRefreshListener {
            layout.home_refresh.isRefreshing = false
            fetchData()
        }
    }

    private fun setupCards() {
        layout.latest_publication.setup({
            Log.d("varlamov", "Go to publication")
            unimplemented()
        })
    }

    private fun fetchData() {
        presenter.loadLatestPost()
        presenter.loadLatestVideos()
    }

    override fun showLatestPublication(publication: Publication) {
        layout.latest_publication.showContent(publication)
    }

    override fun showErrorWhileLoadingLatestPublication() {
        layout.latest_publication.showError()
    }

    override fun showLatestVideos(videos: List<Video>) {}
    override fun showErrorWhileLoadingLatestVideos() {}

    override fun showLatestNews(news: List<Publication>) {}
    override fun showErrorWhileLoadingLatestNews() {}
}
