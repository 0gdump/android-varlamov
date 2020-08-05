package open.v0gdump.varlamov.ui.home.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_news.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.util.toString
import java.util.*

class NewsViewHolder(
    view: View,
    private val onClickListener: (Publication) -> Unit
) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup, onClickListener: (Publication) -> Unit): NewsViewHolder {

            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_news, parent, false)

            return NewsViewHolder(view, onClickListener)
        }
    }

    fun bindTo(publication: Publication) {

        val time = publication.time.toString(TimeZone.getDefault())
        val title = publication.title

        itemView.publication_datetime?.text = time
        itemView.publication_title?.text = title

        itemView.setOnClickListener {
            onClickListener(publication)
        }
    }
}