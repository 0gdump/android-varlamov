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

        setupToolbar()

        setupSwipeRefresh()
        setupCards()

        fetchData()
    }

    private fun setupToolbar() {

        layout.toolbar.title = "Главная"
    }

    private fun setupSwipeRefresh() {
        layout.home_refresh.setOnRefreshListener {
            layout.home_refresh.isRefreshing = false
            fetchData()
        }
    }

    private fun setupCards() {
        layout.fresh_publication_card.setup(
            contentClickListener = {
                Log.d("varlamov", "Go to publication")
                unimplemented()
            },

            retryClickListener = {
                Log.d("varlamov", "Retry load fresh publication")
                unimplemented()
            }
        )

        layout.news_card.setup(
            moreClickListener = {
                Log.d("varlamov", "More news")
                unimplemented()
            },
            retryClickListener = {
                Log.d("varlamov", "Retry load news")
                unimplemented()
            }
        )
    }

    private fun fetchData() {
        presenter.loadFreshPublication()
        presenter.loadLatestVideos()
        presenter.loadNews()
    }

    override fun showFreshPublication(publication: Publication) {
        layout.fresh_publication_card.showContent(publication)
    }

    override fun showErrorWhileLoadingFreshPublication() {
        layout.fresh_publication_card.showError()
    }

    override fun showLatestVideos(videos: List<Video>) {
        Log.d("varlamov", "UNIMPLEMENTED: showLatestVideos")
        unimplemented()
    }

    override fun showErrorWhileLoadingLatestVideos() {
        Log.d("varlamov", "UNIMPLEMENTED: showErrorWhileLoadingLatestVideos")
        unimplemented()
    }

    override fun showNews(news: List<Publication>) {
        layout.news_card.showContent(news)
    }

    override fun showErrorWhileLoadingNews() {
        layout.news_card.showError()
    }
}
