package open.v0gdump.varlamov.ui.fragment.home

import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.fragment_home.view.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.presentation.presenter.main.HomeFragmentPresenterImpl
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.model.platform.youtube.model.Video
import open.v0gdump.varlamov.presentation.view.main.HomeView
import open.v0gdump.varlamov.ui.fragment.MvpFragmentX

class HomeFragment : MvpFragmentX(R.layout.fragment_home),
    HomeView {

    private val presenter by moxyPresenter { HomeFragmentPresenterImpl() }

    override fun setupLayout() {
        setupToolbar()
        setupSwipeRefresh()
        setupCards()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

            retryClickListener = { fetchFreshPublication() }
        )

        layout.videos_card.setup(
            moreClickListener = {
                Log.d("varlamov", "More videos")
                unimplemented()
            },
            retryClickListener = { fetchLatestVideos() }
        )

        layout.news_card.setup(
            moreClickListener = {
                Log.d("varlamov", "More news")
                unimplemented()
            },
            retryClickListener = { fetchNews() }
        )
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
}
