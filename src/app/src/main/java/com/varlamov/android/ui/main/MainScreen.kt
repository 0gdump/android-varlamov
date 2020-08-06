package com.varlamov.android.ui.main

import androidx.navigation.Navigation
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.varlamov.android.R
import com.varlamov.android.ui.global.MvpFragmentX
import com.varlamov.android.util.android.navigation.KeepStateNavigator
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainScreen : MvpFragmentX(R.layout.fragment_main) {

    override fun setupLayoutOnCreate() {
        super.setupLayoutOnCreate()
        Navigation.findNavController(layout.findViewById(R.id.navigationContainer)).let {
            val navigator =
                KeepStateNavigator(activity, childFragmentManager, R.id.navigationContainer)
            it.navigatorProvider += navigator
            it.setGraph(R.navigation.main)
            layout.bottomNavigation.setupWithNavController(it)
        }
    }
}