package open.v0gdump.varlamov.ui.news

import android.os.Bundle
import androidx.core.os.bundleOf
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_by_tag.*
import kotlinx.android.synthetic.main.view_paginal_render.view.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.App
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.presentation.global.Paginator
import open.v0gdump.varlamov.presentation.news.NewsScreenPresenter
import open.v0gdump.varlamov.presentation.news.NewsScreenView
import open.v0gdump.varlamov.ui.global.MvpFragmentX
import open.v0gdump.varlamov.ui.global.PublicationsAdapterDelegate
import open.v0gdump.varlamov.ui.global.adapter.paginal.PaginalAdapter

class NewsScreen : MvpFragmentX(R.layout.fragment_by_tag), NewsScreenView {

    private val presenter by moxyPresenter { NewsScreenPresenter() }

    private val adapter by lazy {
        PaginalAdapter(
            { presenter.loadMore() },
            { o, n ->
                if (o is Publication && n is Publication)
                    o.id == n.id
                else
                    false
            },
            PublicationsAdapterDelegate {
                presenter.onPublicationClicked(it)
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        paginalRenderView.toolbar.title = App.res.getString(R.string.nav_news)
        paginalRenderView.toolbar.inflateMenu(R.menu.toolbar)
        paginalRenderView.toolbar.setOnMenuItemClickListener { activity.onMenuItemSelected(it) }

        paginalRenderView.init(
            { presenter.refresh() },
            adapter
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        Toasty.info(activity, message).show()
    }

    override fun navigateToPublicationScreen(publication: Publication) {
        activity.navigateTo(
            R.id.reader_screen,
            bundleOf("publication" to publication)
        )
    }
}