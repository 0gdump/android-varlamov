package retulff.open.varlamov.ui.adapter.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import retulff.open.varlamov.ui.viewholder.home.YoutubeVideoViewHolder
import retulff.open.varlamov.varlamov.platform.youtube.model.Video

class YoutubeVideosAdapter(private val items: List<Video>) : RecyclerView.Adapter<YoutubeVideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = YoutubeVideoViewHolder.create(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: YoutubeVideoViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}