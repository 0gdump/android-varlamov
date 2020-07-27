package open.v0gdump.varlamov.ui.reader

import android.os.Bundle
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_reader.*
import kotlinx.android.synthetic.main.view_content_render.view.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.presentation.global.Contentator
import open.v0gdump.varlamov.presentation.reader.ReaderScreenPresenter
import open.v0gdump.varlamov.presentation.reader.ReaderScreenView
import open.v0gdump.varlamov.ui.global.MvpFragmentX
import open.v0gdump.varlamov.ui.global.adapter.content.ContentAdapter
import open.v0gdump.varlamov.util.orElse

class ReaderScreen : MvpFragmentX(R.layout.fragment_reader), ReaderScreenView {

    private val presenter by moxyPresenter { ReaderScreenPresenter(app) }

    private val adapter by lazy {
        ContentAdapter(
            { _, _ -> false },
            TextPEAdapterDelegate()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.publication = arguments?.get("publication") as Publication

        // FIXME(CHECK) Is this works???
        contentRenderView.toolbar.setNavigationIcon(R.drawable.ic_back)
        contentRenderView.toolbar.title = presenter.publication?.title.orElse { "Публикация" }

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
        Toasty.info(activity, message).show()
    }
}