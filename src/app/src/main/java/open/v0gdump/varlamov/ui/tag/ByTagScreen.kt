package open.v0gdump.varlamov.ui.tag

import android.os.Bundle
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_by_date.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.presentation.global.Paginator
import open.v0gdump.varlamov.presentation.tag.ByTagScreenPresenter
import open.v0gdump.varlamov.presentation.tag.ByTagScreenView
import open.v0gdump.varlamov.ui.global.MvpFragmentX
import open.v0gdump.varlamov.ui.global.PublicationsAdapterDelegate
import open.v0gdump.varlamov.ui.global.adapter.paginal.PaginalAdapter

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