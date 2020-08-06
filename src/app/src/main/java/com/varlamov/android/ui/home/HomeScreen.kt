package com.varlamov.android.ui.home

import android.os.Bundle
import androidx.core.os.bundleOf
import com.varlamov.android.R
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.model.platform.youtube.model.Video
import com.varlamov.android.presentation.home.HomeScreenPresenter
import com.varlamov.android.presentation.home.HomeScreenView
import com.varlamov.android.ui.global.MvpFragmentX
import kotlinx.android.synthetic.main.fragment_home.view.*
import moxy.ktx.moxyPresenter

// FIXME(CODE) Rewrite this using Contentator
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
        layout.swipeRefresh.setOnRefreshListener {
            layout.swipeRefresh.isRefreshing = false
            fetchData()
        }
    }

    private fun setupCards() {
        layout.freshPublicationCard.setup(
            contentClickListener = { publication ->
                activity.navigateTo(
                    R.id.reader_screen,
                    bundleOf("publication" to publication)
                )
            },
            retryClickListener = { fetchFreshPublication() }
        )

        layout.videosCard.setup(
            moreClickListener = {
                unimplemented()
            },
            retryClickListener = { fetchLatestVideos() }
        )

        layout.newsCard.setup(
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
        layout.freshPublicationCard.showLoading()
        presenter.loadFreshPublication()
    }

    private fun fetchLatestVideos() {
        layout.videosCard.showLoading()
        presenter.loadLatestVideos()
    }

    private fun fetchNews() {
        layout.newsCard.showLoading()
        presenter.loadNews()
    }

    override fun showFreshPublication(publication: Publication) {
        layout.freshPublicationCard.showContent(publication)
    }

    override fun showErrorWhileLoadingFreshPublication() {
        layout.freshPublicationCard.showError()
    }

    override fun showLatestVideos(videos: List<Video>) {
        layout.videosCard.showContent(videos)
    }

    override fun showErrorWhileLoadingLatestVideos() {
        layout.videosCard.showError()
    }

    override fun showNews(news: List<Publication>) {
        layout.newsCard.showContent(news)
    }

    override fun showErrorWhileLoadingNews() {
        layout.newsCard.showError()
    }

    override fun navigateToPublicationScreen(publication: Publication) {
        activity.navigateTo(
            R.id.reader_screen,
            bundleOf("publication" to publication)
        )
    }
}
