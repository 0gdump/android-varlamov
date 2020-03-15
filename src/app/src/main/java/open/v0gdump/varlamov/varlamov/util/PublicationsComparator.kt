package open.v0gdump.varlamov.varlamov.util

import androidx.recyclerview.widget.DiffUtil
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication

val PUBLICATIONS_COMPARATOR = object : DiffUtil.ItemCallback<Publication>() {

    override fun areItemsTheSame(oldItem: Publication, newItem: Publication): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Publication, newItem: Publication): Boolean =
        oldItem.content == newItem.content
}