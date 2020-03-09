package retulff.open.varlamov.ui.view.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_home_card.view.*
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.view.extension.showBeautifulError

@Suppress("MemberVisibilityCanBePrivate")
abstract class HomeCardView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    private lateinit var cardLayout: View
    protected lateinit var contentLayout: View

    private var moreClickListener: (() -> Unit)? = null
    private var retryClickListener: (() -> Unit)? = null

    fun setup(
        title: String,
        parentRes: Int,
        moreClickListener: (() -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = true
    ) {

        cardLayout = LayoutInflater
            .from(context)
            .inflate(R.layout.view_home_card, this, true)

        contentLayout = LayoutInflater
            .from(context)
            .inflate(parentRes, cardLayout.container, true)

        this.moreClickListener = moreClickListener
        this.retryClickListener = retryClickListener

        cardLayout.title.text = title

        if (moreClickListener == null) {
            cardLayout.more.visibility = GONE
        } else {
            cardLayout.more.setOnClickListener {
                this.moreClickListener!!.invoke()
            }
        }

        if (setLoading) {
            showLoading()
        }
    }

    fun showLoading() {
        cardLayout.stateful_container.showLoading(App.res.getString(R.string.state_loading))
    }

    fun showError(message: String) {
        cardLayout.stateful_container.showBeautifulError(
            message,
            App.res.getString(R.string.retry),
            retryClickListener
        )
    }

    protected fun hideOverlay() {
        cardLayout.stateful_container.showContent()
    }

    abstract fun showContent(content: Any)
}