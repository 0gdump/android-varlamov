package com.varlamov.android.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.util.android.showBeautifulError
import com.varlamov.android.util.kotlin.initOnce
import kotlinx.android.synthetic.main.view_home_card.view.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class HomeCardView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    protected var title: String? by initOnce()

    private var cardLayout: View by initOnce()
    protected var contentLayout: View by initOnce()

    protected var contentRes: Int by initOnce()

    protected var moreClickListener: (() -> Unit)? = null
    protected var retryClickListener: (() -> Unit)? = null

    protected fun setup(
        title: String?,
        contentRes: Int,
        moreClickListener: (() -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = true
    ) {
        this.title = title
        this.contentRes = contentRes

        this.moreClickListener = moreClickListener
        this.retryClickListener = retryClickListener

        inflateLayouts()
        setupCardLayout()
        bindListeners()

        if (setLoading) {
            showLoading()
        }
    }

    private fun inflateLayouts() {

        cardLayout = LayoutInflater
            .from(context)
            .inflate(R.layout.view_home_card, this, true)

        contentLayout = LayoutInflater
            .from(context)
            .inflate(contentRes, cardLayout.contentContainer, true)
    }

    private fun setupCardLayout() {

        if (title == null) {
            cardLayout.headerContainer.visibility = GONE
        } else {
            cardLayout.title.text = title
        }

        if (moreClickListener == null) {
            cardLayout.moreButton.visibility = GONE
        }
    }

    private fun bindListeners() {

        cardLayout.moreButton.setOnClickListener {
            this.moreClickListener?.invoke()
        }
    }

    fun showLoading() {
        cardLayout.statefulContainer.showLoading(App.res.getString(R.string.state_loading))
    }

    fun showError() {
        cardLayout.statefulContainer.showBeautifulError(
            App.res.getString(R.string.cant_loading_data),
            App.res.getString(R.string.retry),
            retryClickListener
        )
    }

    abstract fun showContent(content: Any)

    protected fun hideOverlay() {
        cardLayout.statefulContainer.showContent()
    }
}