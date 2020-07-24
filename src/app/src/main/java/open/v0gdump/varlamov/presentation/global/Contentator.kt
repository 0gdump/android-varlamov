package open.v0gdump.varlamov.presentation.global

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

object Contentator {

    sealed class State {
        object Empty : State()
        object EmptyProgress : State()
        data class EmptyError(val error: Throwable) : State()
        data class Data<Item>(val data: List<Item>) : State()
        data class Refresh<Item>(val data: List<Item>) : State()
    }

    sealed class Action {
        object Refresh : Action()
        data class Data<Item>(val items: List<Item>) : Action()
        data class Error(val error: Throwable) : Action()
    }

    sealed class SideEffect {
        object LoadData : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    private fun <Item> reducer(
        action: Action,
        state: State,
        sideEffectListener: (SideEffect) -> Unit
    ): State = when (action) {
        is Action.Refresh -> {
            sideEffectListener(SideEffect.LoadData)
            when (state) {
                is State.Empty -> State.EmptyProgress
                is State.EmptyError -> State.EmptyProgress
                is State.Data<*> -> State.Refresh(
                    state.data as List<Item>
                )
                else -> state
            }
        }
        is Action.Data<*> -> {
            val items = action.items as List<Item>
            when (state) {
                is State.EmptyProgress -> State.Data(
                    items
                )
                is State.Refresh<*> -> State.Data(
                    items
                )
                else -> state
            }
        }
        is Action.Error -> {
            when (state) {
                is State.EmptyProgress -> State.EmptyError(
                    action.error
                )
                is Paginator.State.Refresh<*> -> {
                    sideEffectListener(
                        SideEffect.ErrorEvent(
                            action.error
                        )
                    )
                    State.Data(
                        state.data as List<Item>
                    )
                }
                else -> state
            }
        }
    }

    class Store<T> : CoroutineScope by CoroutineScope(Dispatchers.Default) {
        private var state: State = State.Empty
        var render: (State) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }

        val sideEffects = Channel<SideEffect>()

        fun proceed(action: Action) {
            val newState = reducer<T>(action, state) { sideEffect ->
                launch { sideEffects.send(sideEffect) }
            }
            if (newState != state) {
                state = newState
                render(state)
            }
        }
    }
}