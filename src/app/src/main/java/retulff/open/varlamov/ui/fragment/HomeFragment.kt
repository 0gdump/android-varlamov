package retulff.open.varlamov.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.view.*
import retulff.open.varlamov.R
import retulff.open.varlamov.ui.activity.MainActivity
import retulff.open.varlamov.ui.view.home.HomeCardView

class HomeFragment : Fragment() {

    private lateinit var layout: View

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        layout = inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )

        setupToolbar()
        setupSwipeRefresh()

        loadData()

        return layout
    }

    private fun setupToolbar() {

        (activity as MainActivity).setToolbarTitle(R.string.nav_home)
    }

    private fun setupSwipeRefresh() {
        layout.home_refresh.setOnRefreshListener {

            layout.home_refresh.isRefreshing = false
            loadData(true)
        }
    }

    private fun loadData(forcibly: Boolean = false) {
        for (i in 0 until layout.cards_container.childCount) {
            (layout.cards_container.getChildAt(i) as HomeCardView).loadData(forcibly)
        }
    }
}
