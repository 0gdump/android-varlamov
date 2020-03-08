package retulff.open.varlamov.ui.viewholder.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_news.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.util.TimeUtils
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import java.util.*

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {

            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_news, parent, false)

            return NewsViewHolder(view)
        }
    }

    private lateinit var context: Context
    private lateinit var publication: Publication

    fun bindTo(publication: Publication) {

        this.context = itemView.context
        this.publication = publication

        bindContent()
        bindListener()
    }

    private fun bindContent() {

        val time = TimeUtils.dateTimeToString(publication.time, TimeZone.getDefault())
        val title = publication.title

        itemView.publication_datetime?.text = time
        itemView.publication_title?.text = title
    }

    private fun bindListener() {
        itemView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(publication.url))
            context.startActivity(browserIntent)
        }
    }
}