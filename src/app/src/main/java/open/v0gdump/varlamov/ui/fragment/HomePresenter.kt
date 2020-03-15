package open.v0gdump.varlamov.ui.fragment

import moxy.MvpPresenter

abstract class HomePresenter : MvpPresenter<HomeView>() {

    abstract fun loadFreshPublication()
    abstract fun loadLatestVideos()
    abstract fun loadNews()
}