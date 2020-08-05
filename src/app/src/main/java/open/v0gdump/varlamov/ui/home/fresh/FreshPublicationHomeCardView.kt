package open.v0gdump.varlamov.ui.home.fresh

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_home_latest_publication.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.ui.home.HomeCardView
import open.v0gdump.varlamov.util.toString
import java.util.*

class FreshPublicationHomeCardView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    private lateinit var publication: Publication
    private var contentClickListener: ((Any?) -> Unit)? = null

    fun setup(
        contentClickListener: ((Any?) -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = false
    ) {
        this.contentClickListener = contentClickListener
        super.setup(
            null,
            R.layout.view_home_latest_publication,
            null,
            retryClickListener,
            setLoading
        )
    }

    override fun showContent(content: Any) {

        if (content !is Publication) {
            throw RuntimeException("Method showContent requires Publication, given ${content.javaClass}")
        }

        publication = content

        tryToLoadPreview(content.content)

        contentLayout.date.text = content.time.toString(TimeZone.getDefault())
        contentLayout.title.text = content.title

        contentLayout.freshPublicationContainer.setOnClickListener {
            contentClickListener?.invoke(content)
        }

        hideOverlay()
    }

    private fun tryToLoadPreview(publicationContent: String) {
        val regex = Regex("src=\"([^\"]+)\"")
        val matchResult = regex.find(publicationContent) ?: return

        contentLayout.preview.visibility = VISIBLE

        val glideOptions = RequestOptions()
            .centerCrop()
            .placeholder(ColorDrawable(Color.LTGRAY))

        Glide
            .with(this)
            .load(matchResult.groupValues[1])
            .apply(glideOptions)
            .into(contentLayout.preview)
    }
}