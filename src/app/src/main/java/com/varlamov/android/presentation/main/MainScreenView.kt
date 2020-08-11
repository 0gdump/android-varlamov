package com.varlamov.android.presentation.main

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainScreenView : MvpView {

    fun showFragment(fragment: Int)
}