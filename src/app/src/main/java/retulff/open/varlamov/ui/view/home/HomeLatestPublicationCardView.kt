package retulff.open.varlamov.ui.view.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_home_latest_publication.view.*
import retulff.open.varlamov.util.TimeUtils
import retulff.open.varlamov.varlamov.platform.livejournal.model.Publication
import java.util.*

class HomeLatestPublicationCardView(
    context: Context,
    attrs: AttributeSet
) : HomeCardView(context, attrs) {

    override fun showContent(content: Any) {

        if (content !is Publication) {
            throw RuntimeException("Method showContent requires Publication, given ${content.javaClass}")
        }

        tryToLoadPreview(content.content)

        contentLayout.date.text = TimeUtils.dateTimeToString(content.time, TimeZone.getDefault())
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