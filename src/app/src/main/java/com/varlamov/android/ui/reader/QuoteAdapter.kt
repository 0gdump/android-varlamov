package com.varlamov.android.ui.reader

import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varlamov.android.R
import com.varlamov.android.model.reader.QuoteElement
import com.varlamov.android.util.android.inflate
import kotlinx.android.synthetic.main.item_publication_quote_part.view.*

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
            holder.itemView.text.text =
                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.itemView.text.text = Html.fromHtml(html)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}