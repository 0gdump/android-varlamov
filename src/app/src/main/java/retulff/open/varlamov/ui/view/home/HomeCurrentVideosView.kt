package retulff.open.varlamov.ui.view.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.view_home_current_videos.view.*
import retulff.open.varlamov.App
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.adapter.home.YoutubeVideosAdapter
import retulff.open.varlamov.util.extension.orElse
import retulff.open.varlamov.varlamov.SocialNetworks
import retulff.open.varlamov.viewmodel.home.HomeCurrentVideosViewModel

class HomeCurrentVideosView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    private val layout: View = super.setupCard(
        App.res.getString(R.string.current_videos),
        R.layout.view_home_current_videos
    )

    private val model by lazy {
        ViewModelProviders.of(getActivity()!!).get(HomeCurrentVideosViewModel::class.java)
    }

    init {

        // Костыль. Реализация карусели через SnapHelper путем установки ширины в match_parent
        layout.youtube_videos_recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(layout.youtube_videos_recycler)

        setMoreClickListener(OnClickListener {
            startActivity(
                getActivity()!!,
                Intent(Intent.ACTION_VIEW, Uri.parse("${SocialNetworks.YOUTUBE_URL}/videos")),
                null
            )
        })
    }

    override fun loadData(forcibly: Boolean) {

        showLoading()

        val pub = model.getCurrentVideos().value

        // Данные прежде не были загружены или не вызвано принудительное обновление
        if (pub == null || forcibly) {

            model.getCurrentVideos().observe(getActivity()!!, Observer {
                showData()
            })
            model.loadData()

        } else {
            showData()
        }
    }

    private fun showData() {
        model.getCurrentVideos().value?.let { videos ->

            val adapter = YoutubeVideosAdapter(videos)
            layout.youtube_videos_recycler.adapter = adapter

            showContent()

        }.orElse {
            showError(App.res.getString(R.string.home_current_videos)) { loadData() }
        }
    }
}