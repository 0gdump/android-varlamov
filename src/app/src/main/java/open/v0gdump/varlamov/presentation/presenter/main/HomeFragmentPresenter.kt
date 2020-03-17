package open.v0gdump.varlamov.presentation.presenter.main

import moxy.MvpPresenter
import open.v0gdump.varlamov.presentation.view.main.HomeView

abstract class HomeFragmentPresenter : MvpPresenter<HomeView>() {

    abstract fun loadFreshPublication()
    abstract fun loadLatestVideos()
    abstract fun loadNews()
}