package com.varlamov.android.ui.reader.delegate

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.varlamov.android.R
import com.varlamov.android.model.reader.InstagramPublicationElement
import com.varlamov.android.util.android.inflate

class InstagramElementDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(parent.inflate(R.layout.item_publication_instagram))

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is InstagramPublicationElement

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val url = (items[position] as InstagramPublicationElement).url
        holder.itemView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            holder.itemView.context.startActivity(browserIntent)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}