package retulff.open.varlamov.ui.view.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.view_home_latest_news.view.*
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.activity.MainActivity
import retulff.open.varlamov.ui.adapter.home.NewsAdapter
import retulff.open.varlamov.util.extension.orElse
import retulff.open.varlamov.viewmodel.home.HomeLatestNewsViewModel

class HomeLatestNewsView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    private val layout: View = super.setupCard(
        App.res.getString(R.string.latest_news),
        R.layout.view_home_latest_news
    )

    private val model by lazy {
        ViewModelProviders.of(getActivity()!!).get(HomeLatestNewsViewModel::class.java)
    }

    init {

        layout.news_recycler.layoutManager = LinearLayoutManager(context)

        setMoreClickListener(OnClickListener {
            (getActivity()!! as MainActivity).navigateTo(R.id.nav_news)
        })
    }

    override fun loadData(forcibly: Boolean) {

        showLoading()

        val pub = model.getLatestNews().value

        // Данные прежде не были загружены или не вызвано принудительное обновление
        if (pub == null || forcibly) {

            model.getLatestNews().observe(getActivity()!!, Observer {
                showData()
            })
            model.loadData()

        } else {
            showData()
        }
    }

    private fun showData() {
        model.getLatestNews().value?.let { publications ->

            val adapter = NewsAdapter(publications)
            layout.news_recycler.adapter = adapter

            showContent()

        }.orElse {
            showError(App.res.getString(R.string.home_recent_publication)) { loadData() }
        }
    }
}