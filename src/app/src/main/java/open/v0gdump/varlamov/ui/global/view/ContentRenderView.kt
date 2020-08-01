package open.v0gdump.varlamov.ui.global.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.view_content_render.view.*
import kotlinx.android.synthetic.main.view_paginal_render.view.emptyView
import kotlinx.android.synthetic.main.view_paginal_render.view.fullscreenProgressView
import kotlinx.android.synthetic.main.view_paginal_render.view.recyclerView
import kotlinx.android.synthetic.main.view_paginal_render.view.swipeToRefresh
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.presentation.global.Contentator
import open.v0gdump.varlamov.ui.global.adapter.content.ContentAdapter
import open.v0gdump.varlamov.util.inflate
import open.v0gdump.varlamov.util.visible

class ContentRenderView : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var refreshCallback: (() -> Unit)? = null

    private var adapter: ContentAdapter? = null

    init {
        inflate(R.layout.view_content_render, true)
        swipeToRefresh.setOnRefreshListener { refreshCallback?.invoke() }
        emptyView.setRefreshListener { refreshCallback?.invoke() }

        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun init(
        refreshCallback: () -> Unit,
        adapter: ContentAdapter
    ) {
        this.refreshCallback = refreshCallback
        this.adapter = adapter
        recyclerView.adapter = adapter
    }

    fun render(state: Contentator.State) = post {
        when (state) {
            is Contentator.State.Empty -> {
                appBar.visible(false)
                swipeToRefresh.visible(false)
                swipeToRefresh.isRefreshing = false
                fullscreenProgressView.visible(false)
                adapter?.update(emptyList())
                emptyView.showEmptyData()
            }
            is Contentator.State.EmptyProgress -> {
                appBar.visible(false)
                swipeToRefresh.visible(false)
                swipeToRefresh.isRefreshing = false
                fullscreenProgressView.visible(true)
                adapter?.update(emptyList())
                emptyView.hide()
            }
            is Contentator.State.EmptyError -> {
                appBar.visible(false)
                swipeToRefresh.visible(true)
                swipeToRefresh.isRefreshing = false
                fullscreenProgressView.visible(true)
                adapter?.update(emptyList())
                emptyView.showEmptyError(state.error.localizedMessage)
            }
            is Contentator.State.Data<*> -> {
                appBar.visible(true)
                swipeToRefresh.visible(true)
                swipeToRefresh.isRefreshing = false
                fullscreenProgressView.visible(false)
                adapter?.update(state.data as List<Any>)
                emptyView.hide()
            }
            is Contentator.State.Refresh<*> -> {
                appBar.visible(true)
                swipeToRefresh.visible(true)
                swipeToRefresh.isRefreshing = true
                fullscreenProgressView.visible(false)
                // FIXME(HACK) Disable update bc data is loss in post block
                //adapter?.update(state.data as List<Any>)
                emptyView.hide()
            }
        }
    }
}
