package com.varlamov.android.ui.date

import android.os.Bundle
import androidx.core.os.bundleOf
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.presentation.date.ByDateScreenPresenter
import com.varlamov.android.presentation.date.ByDateScreenView
import com.varlamov.android.presentation.global.Paginator
import com.varlamov.android.ui.global.MvpFragmentX
import com.varlamov.android.ui.global.PublicationsAdapterDelegate
import com.varlamov.android.ui.global.adapter.paginal.PaginalAdapter
import com.varlamov.android.util.kotlin.argument
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_by_date.*
import kotlinx.android.synthetic.main.view_paginal_render.view.*
import moxy.ktx.moxyPresenter

class ByDateScreen : MvpFragmentX(R.layout.fragment_by_date), ByDateScreenView {

    private val presenter by moxyPresenter { ByDateScreenPresenter(dateTimestamp) }

    private val dateTimestamp by argument(ARG_DATE_TIMESTAMP, 0L)

    companion object {
        private const val ARG_DATE_TIMESTAMP = "arg_date_timestamp"
        fun create(dateTimestamp: Long) =
            ByDateScreen().apply {
                arguments = Bundle().apply {
                    putLong(ARG_DATE_TIMESTAMP, dateTimestamp)
                }
            }
    }

    private val adapter by lazy {
        PaginalAdapter(
            { /* Do nothing */ },
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

        paginalRenderView.toolbar.setNavigationIcon(R.drawable.ic_back)
        paginalRenderView.toolbar.setNavigationOnClickListener { App.router.exit() }
        paginalRenderView.toolbar.title = presenter.day.toString("d MMMM yyyy")

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