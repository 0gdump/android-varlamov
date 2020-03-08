package retulff.open.varlamov.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_by_tag.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.adapter.PublicationsAdapter
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.repository.InMemoryByItemRepository
import retulff.open.varlamov.viewmodel.PublicationsByTagViewModel
import java.util.concurrent.Executors

open class ByTagFragment : Fragment() {

    protected lateinit var layout: View
    protected val model by lazy {
        ViewModelProviders.of(activity!!, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                val repo = InMemoryByItemRepository(Executors.newFixedThreadPool(5))

                @Suppress("UNCHECKED_CAST")
                return PublicationsByTagViewModel(repo) as T
            }
        })[PublicationsByTagViewModel::class.java]
    }

    private var publicationsTag: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        layout = inflater.inflate(
            R.layout.fragment_by_tag,
            container,
            false
        )

        extractPassedData()

        setupRecycler()
        initAdapter()
        initSwipeToRefresh()

        return layout
    }

    private fun extractPassedData() {
        arguments?.let {
            publicationsTag = it["TAG"] as String
        }
    }

    private fun setupRecycler() {

        layout.by_tag_recycler.layoutManager = LinearLayoutManager(activity)
    }

    private fun initAdapter() {

        val adapter = PublicationsAdapter({ model.retry() }, publicationsTag)

        layout.by_tag_recycler.adapter = adapter

        model.posts.observe(this, Observer<PagedList<Publication>> { adapter.submitList(it) })
        model.networkState.observe(this, Observer { adapter.setNetworkState(it) })
    }

    private fun initSwipeToRefresh() {

        model.refreshState.observe(this, Observer {
            layout.by_tag_swipe_to_refresh.isRefreshing = false
        })

        layout.by_tag_swipe_to_refresh.setOnRefreshListener { model.refresh() }
    }

    open fun showPublications() {

        // Прямой доступ к списку аргументов осуществялется из-за того, что данный метод
        //   срабатывает быстрее, чем onCreateView и publicationsTag остается null
        model.showByTag(arguments?.get("TAG") as String)
    }
}