package com.varlamov.android.ui.home.news

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.ui.global.DividerItemDecorator
import com.varlamov.android.ui.home.HomeCardView
import kotlinx.android.synthetic.main.view_paginal_render.view.*

class NewsHomeCardView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    private lateinit var onClickListener: (Publication) -> Unit

    fun setup(
        onClickListener: (Publication) -> Unit,
        moreClickListener: (() -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = false
    ) {
        this.onClickListener = onClickListener
        super.setup(
            App.res.getString(R.string.home_latest_news),
            R.layout.view_home_latest_news,
            moreClickListener,
            retryClickListener,
            setLoading
        )

        contentLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        contentLayout.recyclerView.addItemDecoration(DividerItemDecorator(context))
    }

    override fun showContent(content: Any) = when {
        content !is List<*> -> {
            throw RuntimeException("Method showContent requires List<Publication>, given ${content.javaClass}")
        }
        content.isEmpty() -> {
            throw RuntimeException("Method showContent requires non-empty List<Publication>!")
        }
        content.first() !is Publication -> {
            throw RuntimeException("Method showContent requires List<Publication>, given ${content.first()!!.javaClass}")
        }
        else -> {
            contentLayout.recyclerView.adapter =
                NewsAdapter(content as List<Publication>, onClickListener)
            hideOverlay()
        }
    }
}