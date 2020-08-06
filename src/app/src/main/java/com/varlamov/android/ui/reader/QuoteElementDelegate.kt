package com.varlamov.android.ui.reader

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.varlamov.android.R
import com.varlamov.android.model.reader.QuotePublicationElement
import com.varlamov.android.util.android.inflate
import kotlinx.android.synthetic.main.item_publication_quote.view.*

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