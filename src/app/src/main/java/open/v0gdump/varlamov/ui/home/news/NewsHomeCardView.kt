package open.v0gdump.varlamov.ui.home.news

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.view_home_latest_news.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.ui.global.DividerItemDecorator
import open.v0gdump.varlamov.ui.home.HomeCardView

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
            "Последние новости",
            R.layout.view_home_latest_news,
            moreClickListener,
            retryClickListener,
            setLoading
        )

        contentLayout.news_recycler.layoutManager = LinearLayoutManager(context)
        contentLayout.news_recycler.addItemDecoration(DividerItemDecorator(context))
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
            contentLayout.news_recycler.adapter =
                NewsAdapter(content as List<Publication>, onClickListener)
            hideOverlay()
        }
    }
}