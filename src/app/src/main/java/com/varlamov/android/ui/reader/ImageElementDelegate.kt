package com.varlamov.android.ui.reader

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.varlamov.android.R
import com.varlamov.android.model.reader.ImagePublicationElement
import com.varlamov.android.util.android.inflate
import kotlinx.android.synthetic.main.item_publication_image.view.*

class ImageElementDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(parent.inflate(R.layout.item_publication_image))

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is ImagePublicationElement

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val url = (items[position] as ImagePublicationElement).url
        val glideOptions = RequestOptions()
            .placeholder(ColorDrawable(Color.LTGRAY))

        Glide
            .with(holder.itemView.context)
            .load(url)
            .apply(glideOptions)
            .into(holder.itemView.image)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
