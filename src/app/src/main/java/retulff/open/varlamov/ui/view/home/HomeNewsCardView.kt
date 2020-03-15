package retulff.open.varlamov.ui.view.home

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.view_home_latest_news.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.adapter.home.NewsAdapter
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication

class HomeNewsCardView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    fun setup(
        moreClickListener: (() -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = true
    ) {
        super.setup(
            "Последние новости",
            R.layout.view_home_latest_news,
            moreClickListener,
            null,
            retryClickListener,
            setLoading
        )

        contentLayout.news_recycler.layoutManager = LinearLayoutManager(context)
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
            contentLayout.news_recycler.adapter = NewsAdapter(content as List<Publication>)
            hideOverlay()
        }
    }
}