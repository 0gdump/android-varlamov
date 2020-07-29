package open.v0gdump.varlamov.ui.reader

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_publication_text.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.reader.UnimplementedPublicationElement
import open.v0gdump.varlamov.util.inflate

class UnimplementedElementDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(parent.inflate(R.layout.item_publication_unimplemented))

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is UnimplementedPublicationElement

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.itemView.textView.text =
            ("Unimplemented " + (items[position] as UnimplementedPublicationElement).selector)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}