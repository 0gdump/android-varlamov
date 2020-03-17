package open.v0gdump.varlamov.old.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_by_tag.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.old.adapter.PublicationsAdapter
import open.v0gdump.varlamov.extension.orElse

class NewsFragment : ByTagFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        showPublications()

        return layout
    }

    override fun showPublications() {
        model.posts.value?.let {
            (layout.by_tag_recycler.adapter as PublicationsAdapter).submitList(it)
        }.orElse {
            model.showByTag(getString(R.string.news_tag))
        }
    }
}