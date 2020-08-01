package open.v0gdump.varlamov.ui.reader

import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_publication_quote_part.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.reader.quote.QuoteElement
import open.v0gdump.varlamov.util.inflate

class QuoteAdapter(val items: List<QuoteElement>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(parent.inflate(R.layout.item_publication_quote_part))

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val html = items[position].html

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.itemView.textView.text =
                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.itemView.textView.text = Html.fromHtml(html)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}