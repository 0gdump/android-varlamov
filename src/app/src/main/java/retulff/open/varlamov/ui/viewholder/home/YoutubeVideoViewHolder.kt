package retulff.open.varlamov.ui.viewholder.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_youtube_video.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.varlamov.platform.youtube.model.Video

class YoutubeVideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): YoutubeVideoViewHolder {

            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_youtube_video, parent, false)

            return YoutubeVideoViewHolder(view)
        }
    }

    private lateinit var context: Context
    private lateinit var video: Video

    fun bindTo(video: Video) {

        this.context = itemView.context
        this.video = video

        bindPreview()
        bindName()
        bindListener()
    }

    private fun bindPreview() {

        val requestOptions = RequestOptions()
            .placeholder(ColorDrawable(Color.LTGRAY))
            .fitCenter()
            .dontAnimate()

        Glide
            .with(context)
            .setDefaultRequestOptions(requestOptions)
            .load("https://img.youtube.com/vi/${video.id.videoId}/maxresdefault.jpg")
            .into(itemView.video_preview)
    }

    private fun bindName() {
        itemView.video_name.text = video.snippet.title
    }

    private fun bindListener() {
        itemView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/${video.id.videoId}"))
            context.startActivity(browserIntent)
        }
    }
}