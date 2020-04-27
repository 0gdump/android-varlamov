package open.v0gdump.varlamov.ui.publications

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
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

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

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

            itemView.publication_datetime.text = time
            itemView.publication_title.text = title
            itemView.publication_text.text = text
        }

        private fun loadPreview() {

            val regex = Regex("src=\"([^\"]+)\"")
            val matchResult = regex.find(publication.content) ?: return

            itemView.publication_preview.visible(true)

            val glideOptions = RequestOptions()
                .centerCrop()
                .placeholder(ColorDrawable(Color.LTGRAY))

            Glide
                .with(itemView.context)
                .load(matchResult.groupValues[1])
                .apply(glideOptions)
                .into(itemView.publication_preview)
        }
    }
}
