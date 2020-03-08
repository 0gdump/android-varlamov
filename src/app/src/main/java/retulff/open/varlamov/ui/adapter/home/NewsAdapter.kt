package retulff.open.varlamov.ui.adapter.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import retulff.open.varlamov.ui.viewholder.home.NewsViewHolder
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication

class NewsAdapter(private val items: List<Publication>) : RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder.create(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}