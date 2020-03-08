package retulff.open.varlamov.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_publications.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.activity.MainActivity
import retulff.open.varlamov.ui.adapter.PublicationsAdapter
import retulff.open.varlamov.util.extension.orElse
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import retulff.open.varlamov.varlamov.repository.InMemoryByItemRepository
import retulff.open.varlamov.viewmodel.PublicationsViewModel
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        layout = inflater.inflate(
            R.layout.fragment_publications,
            container,
            false
        )

        setupToolbarTitle()
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

    private fun setupToolbarTitle() {

        (activity as MainActivity).setToolbarTitle(R.string.title_posts)
    }

    private fun loadData() {
        model.posts.value?.let {
            (layout.publications_recycler.adapter as PublicationsAdapter).submitList(it)
        }.orElse {
            model.init()
        }
    }
}
