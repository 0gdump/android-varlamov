package open.v0gdump.varlamov.ui.publications

import android.os.Bundle
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_publications.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.presentation.global.Paginator
import open.v0gdump.varlamov.presentation.publications.PublicationsScreenPresenter
import open.v0gdump.varlamov.presentation.publications.PublicationsScreenView
import open.v0gdump.varlamov.ui.global.MvpFragmentX
import open.v0gdump.varlamov.ui.global.PublicationsAdapterDelegate
import open.v0gdump.varlamov.ui.global.view.PaginalAdapter

class PublicationsScreen : MvpFragmentX(R.layout.fragment_publications), PublicationsScreenView {

    private val presenter by moxyPresenter { PublicationsScreenPresenter() }

    override fun setupLayout() {}

    private val adapter by lazy {
        PaginalAdapter(
            { presenter.loadMore() },
            { o, n ->
                if (o is Publication && n is Publication)
                // TODO Требуется быстро сравнивать 2 публикации, иначе возникают странные дергания
                    false
                else
                    false
            },
            PublicationsAdapterDelegate {
                presenter.onPublicationClicked(
                    it
                )
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
}