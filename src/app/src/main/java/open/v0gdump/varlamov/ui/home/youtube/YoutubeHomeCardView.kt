package open.v0gdump.varlamov.ui.home.youtube

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.view_home_current_videos.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.youtube.model.Video
import open.v0gdump.varlamov.ui.home.HomeCardView

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
            "Новые видео",
            R.layout.view_home_current_videos,
            moreClickListener,
            retryClickListener,
            setLoading
        )

        contentLayout.youtube_videos_recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(contentLayout.youtube_videos_recycler)
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
            contentLayout.youtube_videos_recycler.adapter =
                YoutubeAdapter(
                    content as List<Video>
                )
            hideOverlay()
        }
    }
}