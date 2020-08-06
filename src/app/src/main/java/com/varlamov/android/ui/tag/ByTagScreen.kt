package com.varlamov.android.ui.tag

import android.os.Bundle
import androidx.core.os.bundleOf
import com.varlamov.android.R
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.presentation.global.Paginator
import com.varlamov.android.presentation.tag.ByTagScreenPresenter
import com.varlamov.android.presentation.tag.ByTagScreenView
import com.varlamov.android.ui.global.MvpFragmentX
import com.varlamov.android.ui.global.PublicationsAdapterDelegate
import com.varlamov.android.ui.global.adapter.paginal.PaginalAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_by_tag.*
import kotlinx.android.synthetic.main.view_paginal_render.view.*
import moxy.ktx.moxyPresenter

class ByTagScreen : MvpFragmentX(R.layout.fragment_by_tag), ByTagScreenView {

    private val presenter by moxyPresenter { ByTagScreenPresenter() }

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

        presenter.tag = arguments?.getString("tag")!!

        paginalRenderView.toolbar.title = presenter.tag

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