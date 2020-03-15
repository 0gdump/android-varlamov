package open.v0gdump.varlamov.ui.view.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_home_latest_publication.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.util.extension.toString
import open.v0gdump.varlamov.varlamov.platform.livejournal.model.Publication
import java.util.*

class HomeFreshPublicationCardView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    fun setup(
        contentClickListener: (() -> Unit)? = null,
        retryClickListener: (() -> Unit)? = null,
        setLoading: Boolean = true
    ) {
        super.setup(
            null,
            R.layout.view_home_latest_publication,
            null,
            contentClickListener,
            retryClickListener,
            setLoading
        )
    }

    override fun showContent(content: Any) {

        if (content !is Publication) {
            throw RuntimeException("Method showContent requires Publication, given ${content.javaClass}")
        }

        tryToLoadPreview(content.content)

        contentLayout.date.text = content.time.toString(TimeZone.getDefault())
        contentLayout.title.text = content.title

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