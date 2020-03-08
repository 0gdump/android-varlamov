package retulff.open.varlamov.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_by_tag.*
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.fragment.ByTagFragment

class ByTagActivity : AppCompatActivity() {

    private var tag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_by_tag)

        extractPassedData()
        tag?.let {

            setupToolbar()
            showFragment()
        }
    }

    private fun extractPassedData() {
        intent.extras?.let {
            tag = it["TAG"] as String?
        }
    }

    private fun setupToolbar() {

        setSupportActionBar(toolbar)
        toolbar_title.text = ("#$tag")

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun showFragment() {

        val fragment = ByTagFragment()

        fragment.arguments = Bundle()
        fragment.arguments!!.putString("TAG", tag)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.by_tag_host_fragment, fragment)
            .commit()

        supportFragmentManager.executePendingTransactions()

        fragment.showPublications()
    }
}
