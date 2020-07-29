package open.v0gdump.varlamov.ui.reader

import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_publication_text.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.reader.TextPublicationElement
import open.v0gdump.varlamov.util.inflate

class TextElementDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(parent.inflate(R.layout.item_publication_text))

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is TextPublicationElement

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val text = (items[position] as TextPublicationElement).text

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.itemView.textView.text =
                Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.itemView.textView.text = Html.fromHtml(text)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}