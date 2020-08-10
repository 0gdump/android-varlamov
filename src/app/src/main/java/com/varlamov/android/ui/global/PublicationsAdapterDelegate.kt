package com.varlamov.android.ui.global

import android.R.attr
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.varlamov.android.App
import com.varlamov.android.R
import com.varlamov.android.model.platform.livejournal.model.Publication
import com.varlamov.android.util.android.inflate
import com.varlamov.android.util.android.visible
import com.varlamov.android.util.toString
import kotlinx.android.synthetic.main.item_text_and_image_post.view.*
import java.util.*


class PublicationsAdapterDelegate(
    private val clickListener: (Publication) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    companion object {
        private val glideRequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_preview_wait)
            .error(R.drawable.ic_preview_error)
    }

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
            setListeners()
        }

        // TODO(PERF) Check JodaTime perfomance
        // TODO(CODE) Show text preview
        private fun bindContent() {
            itemView.date.text = publication.time.toString(TimeZone.getDefault())
            itemView.title.text = publication.title

            if (publication.tags.contains(App.res.getString(R.string.news_tag))) {
                showNewsChip()
            } else {
                createTagsChips()
            }
        }

        private fun showNewsChip() {
            itemView.tagsContainer.visible(false)
            itemView.newsLabel.visible(true)
        }

        private fun createTagsChips() {
            itemView.tagsContainer.visible(true)
            itemView.newsLabel.visible(false)

            itemView.tagsContainer.tags = publication.tags
        }

        private fun loadPreview() {
            val previewUrl = extractPreviewUrl()
            if (previewUrl == null) {
                itemView.preview.visible(false)
                return
            }

            itemView.preview.visible(true)

            Glide
                .with(itemView)
                .load(previewUrl)
                .apply(glideRequestOptions)
                .listener(GlideStrangeRequestListener { itemView.preview.visible(false) })
                .into(itemView.preview)
        }

        private fun extractPreviewUrl(): String? {
            val imgSourceRegex = Regex("src=\"([^\"]+)\"")
            val imgSourceMatch = imgSourceRegex.find(publication.content)

            return if (imgSourceMatch == null || imgSourceMatch.groupValues.size < 2) {
                null
            } else {
                imgSourceMatch.groupValues[1]
            }
        }

        private fun setListeners() {
            itemView.setOnClickListener { clickListener.invoke(publication) }
        }
    }
}
