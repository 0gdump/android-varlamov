package open.v0gdump.varlamov.ui.fragment

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
import kotlinx.android.synthetic.main.fragment_publications.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.ui.adapter.PublicationsAdapter
import open.v0gdump.varlamov.util.extension.orElse
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication
import open.v0gdump.varlamov.varlamov.repository.InMemoryByItemRepository
import open.v0gdump.varlamov.viewmodel.PublicationsViewModel
import java.util.concurrent.Executors

class PublicationsFragment : Fragment() {

    private lateinit var layout: View

    private val model: PublicationsViewModel by lazy {
        ViewModelProviders.of(activity!!, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                val repo = InMemoryByItemRepository(Executors.newFixedThreadPool(5))

                @Suppress("UNCHECKED_CAST")
                return PublicationsViewModel(repo) as T
            }
        })[PublicationsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        layout = inflater.inflate(
            R.layout.fragment_publications,
            container,
            false
        )

        setupRecycler()
        initAdapter()
        initSwipeToRefresh()

        loadData()

        return layout
    }

    private fun setupRecycler() {

        layout.publications_recycler.layoutManager = LinearLayoutManager(activity)
    }

    private fun initAdapter() {

        val adapter = PublicationsAdapter({ model.retry() }, null)

        layout.publications_recycler.adapter = adapter

        model.posts.observe(this, Observer<PagedList<Publication>> { adapter.submitList(it) })
        model.networkState.observe(this, Observer { adapter.setNetworkState(it) })
    }

    private fun initSwipeToRefresh() {

        model.refreshState.observe(this, Observer {
            layout.publications_swipe_to_refresh.isRefreshing = false
        })

        layout.publications_swipe_to_refresh.setOnRefreshListener { model.refresh() }
    }

    private fun loadData() {
        model.posts.value?.let {
            (layout.publications_recycler.adapter as PublicationsAdapter).submitList(it)
        }.orElse {
            model.init()
        }
    }
}
