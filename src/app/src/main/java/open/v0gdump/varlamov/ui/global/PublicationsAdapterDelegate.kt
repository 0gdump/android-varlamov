package open.v0gdump.varlamov.ui.global

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_text_and_image_post.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.util.getWords
import open.v0gdump.varlamov.util.inflate
import open.v0gdump.varlamov.util.toString
import open.v0gdump.varlamov.util.visible
import org.jsoup.Jsoup
import java.util.*

class PublicationsAdapterDelegate(
    private val clickListener: (Publication) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is Publication

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_text_and_image_post))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as Publication)

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var publication: Publication

        fun bind(publication: Publication) {

            this.publication = publication

            bindContent()
            loadPreview()
        }

        private fun bindContent() {

            // TODO Check performance
            val time = publication.time.toString(TimeZone.getDefault())
            val title = publication.title
            val itemParsedContent = Jsoup.parse(publication.content)
            val text = "${itemParsedContent.text().getWords(20)}..."

            itemView.date.text = time
            itemView.title.text = title
            //itemView.publication_text.text = text
        }

        private fun loadPreview() {

            val imgSourceRegex = Regex("src=\"([^\"]+)\"")
            val imgSourceMatch = imgSourceRegex.find(publication.content)

            if (imgSourceMatch == null || imgSourceMatch.groupValues.size < 2) {
                itemView.preview.visible(false)
                return
            } else {
                itemView.preview.visible(true)
            }

            val imgSource = imgSourceMatch.groupValues[1]
            val glideOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_preview_wait)
                .error(R.drawable.ic_preview_error)

            Glide
                .with(itemView)
                .load(imgSource)
                .apply(glideOptions)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.preview.visible(false)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ) = false
                })
                .into(itemView.preview)
        }
    }
}
