package open.v0gdump.varlamov.ui.reader

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_publication_image.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.reader.ImagePublicationElement
import open.v0gdump.varlamov.util.inflate

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
            .into(holder.itemView.imageView)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
