package open.v0gdump.varlamov.ui.news

import android.os.Bundle
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_by_tag.*
import moxy.ktx.moxyPresenter
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.presentation.global.Paginator
import open.v0gdump.varlamov.presentation.news.NewsScreenPresenter
import open.v0gdump.varlamov.presentation.news.NewsScreenView
import open.v0gdump.varlamov.ui.global.MvpFragmentX
import open.v0gdump.varlamov.ui.global.PublicationsAdapterDelegate
import open.v0gdump.varlamov.ui.global.view.PaginalAdapter

class NewsScreen : MvpFragmentX(R.layout.fragment_by_tag), NewsScreenView {

    private val presenter by moxyPresenter { NewsScreenPresenter() }

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
                presenter.onPublicationClicked(it)
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