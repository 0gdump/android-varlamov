package com.varlamov.android.ui.home.youtube

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varlamov.android.model.platform.youtube.model.Video

class YoutubeAdapter(private val items: List<Video>) :
    RecyclerView.Adapter<YoutubeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        YoutubeViewHolder.create(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}