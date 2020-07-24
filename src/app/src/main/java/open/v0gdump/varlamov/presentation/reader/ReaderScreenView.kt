package open.v0gdump.varlamov.presentation.reader

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.v0gdump.varlamov.presentation.global.Contentator

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReaderScreenView : MvpView {
    fun renderState(state: Contentator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}