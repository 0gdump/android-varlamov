package open.v0gdump.varlamov.ui.viewholder

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColorStateList
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_text_and_image_post.view.*
import open.v0gdump.varlamov.App
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.ui.activity.ByTagActivity
import open.v0gdump.varlamov.util.extension.getWords
import open.v0gdump.varlamov.util.extension.toString
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

class PublicationViewHolder(
    view: View,
    private val primaryTag: String?
) : RecyclerView.ViewHolder(view) {

    companion object {

        fun create(parent: ViewGroup, primaryTag: String?): PublicationViewHolder {

            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_text_and_image_post, parent, false)

            return PublicationViewHolder(view, primaryTag)
        }
    }

    private lateinit var context: Context
    private lateinit var item: Publication
    private lateinit var itemParsedContent: Document

    fun bindTo(item: Publication) {

        this.context = itemView.context
        this.item = item
        this.itemParsedContent = Jsoup.parse(item.content)

        bindContent()
        bindPreview()
        bindTags()
        bindListeners()
    }

    private fun bindContent() {

        val time = item.time.toString(TimeZone.getDefault())
        val title = item.title
        val text = "${itemParsedContent.text().getWords(20)}..."

        itemView.publication_datetime.text = time
        itemView.publication_title.text = title
        itemView.publication_text.text = text
    }

    private fun bindPreview() {
        val imageNode = itemParsedContent.selectFirst("img")
        if (imageNode != null) {

            itemView.publication_preview.visibility = View.VISIBLE

            val previewUrl = imageNode.attr("src")

            val requestOptions = RequestOptions()
                .placeholder(ColorDrawable(Color.LTGRAY))
                .fitCenter()
                .dontAnimate()

            Glide.with(itemView)
                .setDefaultRequestOptions(requestOptions)
                .load(previewUrl)
                .into(itemView.publication_preview)

        } else {
            itemView.publication_preview.visibility = View.GONE
        }
    }

    private fun bindTags() {

        if (item.tags.contains(App.res.getString(R.string.news_tag))) {

            itemView.publication_tags_container.visibility = View.GONE
            return
        }

        itemView.publication_tags_container.visibility = View.VISIBLE
        itemView.publication_tags.removeAllViews()

        item.tags.forEach {

            val chip = Chip(context)
            chip.text = it

            if (it == primaryTag) {
                chip.chipBackgroundColor = getColorStateList(context, R.color.white)
                chip.isClickable = false
                chip.isEnabled = false
            } else {
                chip.setOnClickListener {
                    val intent = Intent(context, ByTagActivity::class.java)
                    intent.putExtra("TAG", chip.text.toString())
                    startActivity(context, intent, null)
                }
            }

            itemView.publication_tags.addView(chip)
        }
    }

    private fun bindListeners() {
        itemView.setOnClickListener {
            startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(item.url)), null)
        }
    }
}