package open.v0gdump.varlamov.ui.home

import android.os.Bundle
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_home.view.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.platform.youtube.model.Video
import open.v0gdump.varlamov.presentation.home.HomeScreenPresenter
import open.v0gdump.varlamov.presentation.home.HomeScreenView
import open.v0gdump.varlamov.ui.global.MvpFragmentX

class HomeScreen : MvpFragmentX(R.layout.fragment_home), HomeScreenView {

    private val presenter by moxyPresenter { HomeScreenPresenter() }

    override fun setupLayoutOnCreate() {
        setupToolbar()
        setupSwipeRefresh()
        setupCards()
    }

    private fun setupToolbar() {
        layout.toolbar.title = getString(R.string.home_title)
        layout.toolbar.inflateMenu(R.menu.toolbar)
        layout.toolbar.setOnMenuItemClickListener { activity.onMenuItemSelected(it) }
    }

    private fun setupSwipeRefresh() {
        layout.home_refresh.setOnRefreshListener {
            layout.home_refresh.isRefreshing = false
            fetchData()
        }
    }

    private fun setupCards() {
        layout.fresh_publication_card.setup(
            contentClickListener = { publication ->
                activity.navigateTo(
                    R.id.reader_screen,
                    bundleOf("publication" to publication)
                )
            },
            retryClickListener = { fetchFreshPublication() }
        )

        layout.videos_card.setup(
            moreClickListener = {
                unimplemented()
            },
            retryClickListener = { fetchLatestVideos() }
        )

        layout.news_card.setup(
            onClickListener = { publication ->
                activity.navigateTo(
                    R.id.reader_screen,
                    bundleOf("publication" to publication)
                )
            },
            moreClickListener = {
                unimplemented()
            },
            retryClickListener = { fetchNews() }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fetchData()
    }

    private fun fetchData() {
        fetchFreshPublication()
        fetchLatestVideos()
        fetchNews()
    }

    private fun fetchFreshPublication() {
        layout.fresh_publication_card.showLoading()
        presenter.loadFreshPublication()
    }

    private fun fetchLatestVideos() {
        layout.videos_card.showLoading()
        presenter.loadLatestVideos()
    }

    private fun fetchNews() {
        layout.news_card.showLoading()
        presenter.loadNews()
    }

    override fun showFreshPublication(publication: Publication) {
        layout.fresh_publication_card.showContent(publication)
    }

    override fun showErrorWhileLoadingFreshPublication() {
        layout.fresh_publication_card.showError()
    }

    override fun showLatestVideos(videos: List<Video>) {
        layout.videos_card.showContent(videos)
    }

    override fun showErrorWhileLoadingLatestVideos() {
        layout.videos_card.showError()
    }

    override fun showNews(news: List<Publication>) {
        layout.news_card.showContent(news)
    }

    override fun showErrorWhileLoadingNews() {
        layout.news_card.showError()
    }

    override fun navigateToPublicationScreen(publication: Publication) {
        activity.navigateTo(
            R.id.reader_screen,
            bundleOf("publication" to publication)
        )
    }
}
