package open.v0gdump.varlamov.ui.view.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_home_card.view.*
import open.v0gdump.varlamov.App
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.ui.view.extension.showBeautifulError
import open.v0gdump.varlamov.util.initOnce

@Suppress("MemberVisibilityCanBePrivate")
abstract class HomeCardView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    protected var title: String? by initOnce()

    private var cardLayout: View by initOnce()
    protected var contentLayout: View by initOnce()

    protected var contentRes: Int by initOnce()

    private var moreClickListener: (() -> Unit)? = null
    private var contentClickListener: (() -> Unit)? = null
    private var retryClickListener: (() -> Unit)? = null

    protected fun setup(
        title: String?,
        contentRes: Int,
        moreClickListener: (() -> Unit)? = null,
        contentClickListener: (() -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = true
    ) {
        this.title = title
        this.contentRes = contentRes

        this.moreClickListener = moreClickListener
        this.retryClickListener = retryClickListener
        this.contentClickListener = contentClickListener

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
            .inflate(contentRes, cardLayout.container, true)
    }

    private fun setupCardLayout() {

        if (title == null) {
            cardLayout.header_container.visibility = GONE
        } else {
            cardLayout.title.text = title
        }

        if (moreClickListener == null) {
            cardLayout.more.visibility = GONE
        }
    }

    private fun bindListeners() {

        cardLayout.more.setOnClickListener {
            this.moreClickListener?.invoke()
        }

        contentLayout.setOnClickListener {
            this.contentClickListener?.invoke()
        }
    }

    fun showLoading() {
        cardLayout.stateful_container.showLoading(App.res.getString(R.string.state_loading))
    }

    fun showError() {
        cardLayout.stateful_container.showBeautifulError(
            App.res.getString(R.string.cant_loading_data),
            App.res.getString(R.string.retry),
            retryClickListener
        )
    }

    abstract fun showContent(content: Any)

    protected fun hideOverlay() {
        cardLayout.stateful_container.showContent()
    }
}