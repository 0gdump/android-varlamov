package open.v0gdump.varlamov.ui.reader

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_publication_quote.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.reader.QuotePublicationElement
import open.v0gdump.varlamov.util.inflate

class QuoteElementDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(parent.inflate(R.layout.item_publication_quote))

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is QuotePublicationElement

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val paragraphs = (items[position] as QuotePublicationElement).paragraphs

        holder.itemView.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = QuoteAdapter(paragraphs)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}