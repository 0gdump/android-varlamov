package open.v0gdump.varlamov.presentation.news

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.v0gdump.varlamov.model.platform.livejournal.model.Publication
import open.v0gdump.varlamov.presentation.global.Paginator

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsScreenView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToPublicationScreen(publication: Publication)
}