package retulff.open.varlamov.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_varlamov.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.varlamov.SocialNetworks.Companion.FACEBOOK_URL
import retulff.open.varlamov.varlamov.SocialNetworks.Companion.INSTAGRAM_URL
import retulff.open.varlamov.varlamov.SocialNetworks.Companion.TWITTER_URL
import retulff.open.varlamov.varlamov.SocialNetworks.Companion.VK_URL
import retulff.open.varlamov.varlamov.SocialNetworks.Companion.YOUTUBE_URL

class VarlamovFragment : Fragment() {

    private lateinit var layout: View

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        layout = inflater.inflate(
            R.layout.fragment_varlamov,
            container,
            false
        )

        bindLayout()

        return layout
    }

    private fun bindLayout() {

        // Привязка "социальных" кнопок
        layout.social_vk_button.setOnClickListener { openUrlInBrowser(VK_URL) }
        layout.social_facebook_button.setOnClickListener { openUrlInBrowser(FACEBOOK_URL) }
        layout.social_twitter_button.setOnClickListener { openUrlInBrowser(TWITTER_URL) }
        layout.social_instagram_button.setOnClickListener { openUrlInBrowser(INSTAGRAM_URL) }
        layout.social_youtube_button.setOnClickListener { openUrlInBrowser(YOUTUBE_URL) }
    }

    private fun openUrlInBrowser(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}
