package open.v0gdump.varlamov.ui.home.news

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication

class NewsAdapter(
    private val items: List<Publication>,
    private val onClickListener: (Publication) -> Unit
) : RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder.create(parent, onClickListener)

    override fun getItemCount() =
        items.count()

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) =
        holder.bindTo(items[position])
}