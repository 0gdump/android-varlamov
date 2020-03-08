package retulff.open.varlamov.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_by_tag.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.activity.MainActivity
import retulff.open.varlamov.ui.adapter.PublicationsAdapter
import retulff.open.varlamov.util.extension.orElse

class NewsFragment : ByTagFragment() {

    override fun onResume() {

        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        setupTitle()
        showPublications()

        return layout
    }

    private fun setupTitle() {
        (activity as MainActivity).setToolbarTitle(R.string.title_news)
    }

    override fun showPublications() {
        model.posts.value?.let {
            (layout.by_tag_recycler.adapter as PublicationsAdapter).submitList(it)
        }.orElse {
            model.showByTag(getString(R.string.news_tag))
        }
    }
}