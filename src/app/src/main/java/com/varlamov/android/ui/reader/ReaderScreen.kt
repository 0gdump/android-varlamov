package com.varlamov.android.ui.reader

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.presentation.global.Contentator
import com.varlamov.android.presentation.reader.ReaderScreenPresenter
import com.varlamov.android.presentation.reader.ReaderScreenView
import com.varlamov.android.ui.global.MvpFragmentX
import com.varlamov.android.ui.global.adapter.content.ContentAdapter
import com.varlamov.android.util.kotlin.orElse
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_reader.*
import kotlinx.android.synthetic.main.view_content_render.view.*
import moxy.ktx.moxyPresenter

class ReaderScreen : MvpFragmentX(R.layout.fragment_reader), ReaderScreenView {

    private val presenter by moxyPresenter { ReaderScreenPresenter() }

    private val adapter by lazy {
        ContentAdapter(
            { _, _ -> false },
            ImageElementDelegate(),
            TextElementDelegate(),
            QuoteElementDelegate(),
            UnimplementedElementDelegate()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.publication = arguments?.get("publication") as Publication

        contentRenderView.toolbar.setNavigationIcon(R.drawable.ic_back)
        contentRenderView.toolbar.title = presenter.publication?.title
            .orElse { App.res.getString(R.string.publication) }
        contentRenderView.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        contentRenderView.init(
            { presenter.refresh() },
            adapter
        )

        presenter.onActivityCreated()
    }

    override fun renderState(state: Contentator.State) {
        contentRenderView.render(state)
    }

    override fun showMessage(message: String) {
        if (message.isNotBlank()) {
            Toasty.normal(activity, message).show()
        }
    }
}