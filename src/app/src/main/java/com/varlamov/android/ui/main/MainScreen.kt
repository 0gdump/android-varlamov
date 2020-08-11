package com.varlamov.android.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.varlamov.android.R
import com.varlamov.android.Screens
import com.varlamov.android.presentation.main.MainScreenPresenter
import com.varlamov.android.presentation.main.MainScreenView
import com.varlamov.android.ui.global.MvpFragmentX
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.ktx.moxyPresenter

class MainScreen : MvpFragmentX(R.layout.fragment_main), MainScreenView {

    private val presenter by moxyPresenter { MainScreenPresenter() }

    private val fragments = listOf(
        Screens.HomeScreen,
        Screens.PublicationsScreen,
        Screens.NewsScreen
    )

    private var actualFragment: Fragment? = null
    private val actualFragments = mutableListOf<Fragment?>(null, null, null)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemReselectedListener { }
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> presenter.onTabSelected(0)
                R.id.nav_news -> presenter.onTabSelected(1)
                R.id.nav_publications -> presenter.onTabSelected(2)
                else -> presenter.onTabSelected(Int.MAX_VALUE)
            }

            true
        }
    }

    override fun showFragment(index: Int) {

        // Hide current fragment
        if (actualFragment != null) {
            childFragmentManager
                .beginTransaction()
                .hide(actualFragment!!)
                .commit()
        }

        val transaction = childFragmentManager.beginTransaction()

        // Create fragment if not exist
        if (actualFragments[index] == null) {
            actualFragment = fragments[index].fragment
            actualFragments[index] = actualFragment
            transaction.add(R.id.fragmentsContainer, actualFragment!!)
        }
        // Or get it
        else {
            actualFragment = actualFragments[index]
        }

        // Show fragment
        transaction.show(actualFragment!!).commit()
    }
}