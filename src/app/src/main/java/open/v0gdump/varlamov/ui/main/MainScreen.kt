package open.v0gdump.varlamov.ui.main

import androidx.navigation.Navigation
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_main.view.*
import open.v0gdump.varlamov.R
import open.v0gdump.varlamov.ui.global.MvpFragmentX
import open.v0gdump.varlamov.util.navigation.KeepStateNavigator

class MainScreen : MvpFragmentX(R.layout.fragment_main) {

    override fun setupLayoutOnCreate() {
        super.setupLayoutOnCreate()
        Navigation.findNavController(layout.findViewById(R.id.container)).let {
            val navigator = KeepStateNavigator(activity, childFragmentManager, R.id.container)
            it.navigatorProvider += navigator
            it.setGraph(R.navigation.main)
            layout.bottom_navigation.setupWithNavController(it)
        }
    }
}