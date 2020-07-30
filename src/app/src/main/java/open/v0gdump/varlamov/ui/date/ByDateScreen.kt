package open.v0gdump.varlamov.ui.date

import android.os.Bundle
import androidx.core.os.bundleOf
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_by_date.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.presentation.date.ByDateScreenPresenter
import open.v0gdump.varlamov.presentation.date.ByDateScreenView
import open.v0gdump.varlamov.presentation.global.Paginator
import open.v0gdump.varlamov.ui.global.MvpFragmentX
import open.v0gdump.varlamov.ui.global.PublicationsAdapterDelegate
import open.v0gdump.varlamov.ui.global.adapter.paginal.PaginalAdapter

class ByDateScreen : MvpFragmentX(R.layout.fragment_by_date), ByDateScreenView {

    private val presenter by moxyPresenter { ByDateScreenPresenter() }

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

    override fun setupLayoutOnCreate() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.timestamp = arguments?.getLong("dateTimestamp")!!

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