package open.v0gdump.varlamov.ui.fragment.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import open.v0gdump.varlamov.ui.fragment.home.viewholder.YoutubeVideoViewHolder
import open.v0gdump.varlamov.model.platform.youtube.model.Video

class YoutubeVideosAdapter(private val items: List<Video>) :
    RecyclerView.Adapter<YoutubeVideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        YoutubeVideoViewHolder.create(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: YoutubeVideoViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}