package open.v0gdump.varlamov.presentation.reader

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import open.v0gdump.varlamov.presentation.global.Contentator
import open.v0gdump.varlamov.presentation.global.MvpPresenterX

@InjectViewState
class ReaderScreenPresenter : MvpPresenterX<ReaderScreenView>() {

    private val contentator = Contentator.Store<PublicationElement>()

    init {
        contentator.render = { viewState.renderState(it) }
        launch {
            contentator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Contentator.SideEffect.LoadData -> {
                        loadData()
                    }
                    is Contentator.SideEffect.ErrorEvent -> {
                        viewState.showMessage(effect.error.message.orEmpty())
                    }
                }
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refresh()
    }

    private fun loadData() {
        // Recontent!!!
    }

    fun refresh() = contentator.proceed(Contentator.Action.Refresh)
}