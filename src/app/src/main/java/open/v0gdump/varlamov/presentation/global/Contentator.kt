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
        data class Data<T>(val data: List<T>) : State()
        data class Refresh<T>(val data: List<T>) : State()
    }

    sealed class Action {
        object LoadData : Action()
        object Refresh : Action()
        data class Data<T>(val data: List<T>) : Action()
        data class Error(val error: Throwable) : Action()
    }

    sealed class SideEffect {
        object LoadData : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    private fun <T> reducer(
        action: Action,
        state: State,
        sideEffectListener: (SideEffect) -> Unit
    ): State = when (action) {
        is Action.LoadData -> {
            sideEffectListener(SideEffect.LoadData)
            when (state) {
                is State.Empty -> State.EmptyProgress
                is State.EmptyError -> State.EmptyProgress
                else -> state
            }
        }
        is Action.Data<*> -> {
            val items = action.data as List<T>
            when (state) {
                is State.Empty -> State.Data(items)
                is State.EmptyProgress -> State.Data(items)
                is State.EmptyError -> State.Data(items)
                is State.Refresh<*> -> State.Data(items)
                else -> state
            }
        }
        is Action.Refresh -> {
            sideEffectListener(SideEffect.LoadData)
            when (state) {
                is State.Empty -> State.EmptyProgress
                is State.EmptyError -> State.EmptyProgress
                is State.Data<*> -> State.Refresh(state.data as List<T>)
                else -> state
            }
        }
        is Action.Error -> {
            when (state) {
                is State.EmptyProgress -> State.EmptyError(action.error)
                is State.Refresh<*> -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.error))
                    State.Data(state.data as List<T>)
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