package com.varlamov.android.presentation.main

import com.varlamov.android.presentation.global.MvpPresenterX
import moxy.InjectViewState

@InjectViewState
class MainScreenPresenter : MvpPresenterX<MainScreenView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showFragment(0)
    }

    fun onTabSelected(tab: Int) {
        when (tab) {
            0, 1, 2 -> {
                viewState.showFragment(tab)
            }
            else -> throw IllegalArgumentException()
        }
    }
}