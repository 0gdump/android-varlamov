package open.v0gdump.varlamov.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import open.v0gdump.varlamov.ui.viewholder.PublicationViewHolder
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication

class PublicationsSimpleAdapter(
    private val items: List<Publication>
) : RecyclerView.Adapter<PublicationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PublicationViewHolder.create(parent, null)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}