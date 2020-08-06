package com.varlamov.android.ui.reader

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.varlamov.android.R
import com.varlamov.android.model.reader.UnimplementedPublicationElement
import com.varlamov.android.util.android.inflate
import kotlinx.android.synthetic.main.item_publication_text.view.*

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
        holder.itemView.text.text =
            ("Unimplemented " + (items[position] as UnimplementedPublicationElement).selector)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}