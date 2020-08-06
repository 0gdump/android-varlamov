package com.varlamov.android.ui.home.youtube

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
import com.varlamov.android.R
import com.varlamov.android.model.platform.youtube.model.Video
import com.varlamov.android.util.toString
import kotlinx.android.synthetic.main.item_youtube_video.view.*
import org.joda.time.DateTime
import java.util.*

class YoutubeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): YoutubeViewHolder {

            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_youtube_video, parent, false)

            return YoutubeViewHolder(
                view
            )
        }
    }

    private lateinit var context: Context
    private lateinit var video: Video

    fun bindTo(video: Video) {

        this.context = itemView.context
        this.video = video

        bindDate()
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
            .into(itemView.preview)
    }

    private fun bindDate() {
        itemView.publishDate.text = DateTime
            .parse(video.snippet.publishedAt)
            .toString(TimeZone.getDefault())
    }

    private fun bindName() {
        itemView.title.text = video.snippet.title
    }

    private fun bindListener() {
        itemView.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/${video.id.videoId}"))
            context.startActivity(browserIntent)
        }
    }
}