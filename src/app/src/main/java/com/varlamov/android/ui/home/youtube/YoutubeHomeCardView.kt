package com.varlamov.android.ui.home.youtube

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.model.platform.youtube.model.Video
import com.varlamov.android.ui.home.HomeCardView
import kotlinx.android.synthetic.main.view_home_latest_news.view.*

class YoutubeHomeCardView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    fun setup(
        moreClickListener: (() -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = false
    ) {
        super.setup(
            App.res.getString(R.string.home_youtube_videos),
            R.layout.view_home_current_videos,
            moreClickListener,
            retryClickListener,
            setLoading
        )

        contentLayout.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(contentLayout.recyclerView)
    }

    override fun showContent(content: Any) = when {
        content !is List<*> -> {
            throw RuntimeException("Method showContent requires List<Publication>, given ${content.javaClass}")
        }
        content.isEmpty() -> {
            throw RuntimeException("Method showContent requires non-empty List<Publication>!")
        }
        content.first() !is Video -> {
            throw RuntimeException("Method showContent requires List<Publication>, given ${content.first()!!.javaClass}")
        }
        else -> {
            contentLayout.recyclerView.adapter =
                YoutubeAdapter(
                    content as List<Video>
                )
            hideOverlay()
        }
    }
}