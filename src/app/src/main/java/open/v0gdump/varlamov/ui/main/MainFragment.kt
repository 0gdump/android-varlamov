package open.v0gdump.varlamov.ui.main

import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_main.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.ui.global.MvpFragmentX

class MainFragment : MvpFragmentX(R.layout.fragment_main) {

    private val navController by lazy {
        Navigation.findNavController(
            layout.findViewById(R.id.container)
        )
    }

    override fun setupLayout() {
        layout.bottom_navigation.setupWithNavController(navController)
    }
}