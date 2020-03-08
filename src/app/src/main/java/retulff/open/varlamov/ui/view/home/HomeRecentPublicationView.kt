package retulff.open.varlamov.ui.view.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_home_recent_publication.view.*
import org.jsoup.Jsoup
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.activity.MainActivity
import retulff.open.varlamov.util.TimeUtils
import retulff.open.varlamov.util.extension.orElse
import retulff.open.varlamov.util.openUrlInBrowser
import retulff.open.varlamov.viewmodel.home.HomeRecentPublicationViewModel
import java.util.*

class HomeRecentPublicationView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    private val layout: View = super.setupCard(
        App.res.getString(R.string.recent_publication),
        R.layout.view_home_recent_publication
    )

    private val model by lazy {
        ViewModelProviders.of(getActivity()!!).get(HomeRecentPublicationViewModel::class.java)
    }

    init {

        setMoreClickListener(OnClickListener {
            (getActivity()!! as MainActivity).navigateTo(R.id.nav_publications)
        })

        layout.setOnClickListener {
            openUrlInBrowser(context, model.getRecentPublication().value!!.url)
        }
    }

    override fun loadData(forcibly: Boolean) {

        showLoading()

        val pub = model.getRecentPublication().value

        // Данные прежде не были загружены или не вызвано принудительное обновление
        if (pub == null || forcibly) {

            model.getRecentPublication().observe(getActivity()!!, Observer {
                showData()
            })
            model.loadData()

        } else {
            showData()
        }
    }

    private fun showData() {
        model.getRecentPublication().value?.let { publication ->

            // Preview
            Jsoup
                .parse(publication.content)
                ?.selectFirst("img")
                ?.let {

                    layout.preview.visibility = View.VISIBLE

                    Glide
                        .with(this)
                        .load(it.attr("src"))
                        .apply(
                            RequestOptions()
                                .centerCrop()
                                .placeholder(ColorDrawable(Color.LTGRAY))
                        )
                        .into(layout.preview)
                }

            // Content
            layout.date.text = TimeUtils.dateTimeToString(publication.time, TimeZone.getDefault())
            layout.title.text = publication.title

            showContent()

        }.orElse {
            showError(App.res.getString(R.string.home_recent_publication)) { loadData() }
        }
    }
}