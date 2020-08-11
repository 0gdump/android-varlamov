package com.varlamov.android.ui.reader

import android.os.Bundle
import androidx.core.app.ShareCompat
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.presentation.global.Contentator
import com.varlamov.android.presentation.reader.ReaderScreenPresenter
import com.varlamov.android.presentation.reader.ReaderScreenView
import com.varlamov.android.ui.global.MvpFragmentX
import com.varlamov.android.ui.global.adapter.content.ContentAdapter
import com.varlamov.android.util.kotlin.argument
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_reader.*
import kotlinx.android.synthetic.main.view_content_render.view.*
import moxy.ktx.moxyPresenter

class ReaderScreen : MvpFragmentX(R.layout.fragment_reader), ReaderScreenView {

    private val presenter by moxyPresenter { ReaderScreenPresenter(publication) }

    private val publication by argument<Publication>(ARG_PUBLICATION, null)

    companion object {
        private const val ARG_PUBLICATION = "arg_publication"
        fun create(publication: Publication) =
            ReaderScreen().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PUBLICATION, publication)
                }
            }
    }

    private val adapter by lazy {
        ContentAdapter(
            { _, _ -> false },
            ImageElementDelegate(),
            TextElementDelegate(),
            QuoteElementDelegate(),
            TwitterElementDelegate(),
            UnimplementedElementDelegate()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        contentRenderView.init({ presenter.refresh() }, adapter)
        presenter.onActivityCreated()
    }

    private fun setupToolbar() = contentRenderView.toolbar.apply {
        title = presenter.publication.title

        setNavigationIcon(R.drawable.ic_back)
        setNavigationOnClickListener { App.router.exit() }

        contentRenderView.toolbar.inflateMenu(R.menu.reader)
        setOnMenuItemClickListener { shareUrl(); true }
    }

    // TODO(CODE) Move inside presenter
    private fun shareUrl() {
        ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setChooserTitle(App.res.getString(R.string.share_url))
            .setText(presenter.publication.url)
            .startChooser()
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