package com.iliatokarev.pizzeriano161.presentation.main

import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel

class MainViewModel(
) : BasicViewModel<MainUiState, MainUiEvent, MainUiIntent>(MainUiState.State()) {

    override fun setEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.GoToAllPizzaScreen -> {
                setIntent(MainUiIntent.GoToAllPizzaScreenIntent)
            }

            is MainUiEvent.GoToCompletedOrdersScreen -> {
                setIntent(MainUiIntent.GoToCompletedOrdersScreenIntent)
            }

            is MainUiEvent.GoToNewOrdersScreen -> {
                setIntent(MainUiIntent.GoToNewOrdersScreenIntent)
            }

            is MainUiEvent.GoBack -> {
                setIntent(MainUiIntent.GoBackIntent)
            }

            is MainUiEvent.GoToEditPizzaScreen -> {
                setIntent(MainUiIntent.GoToEditPizzaScreen(event.pizzaId))
            }
        }
    }
}

interface MainUiState : BasicUiState {
    class State(
        val isShown: Boolean = true,
        val isLoading: Boolean = false,
        val isErrorMessage: String? = null
    ) : MainUiState
}

sealed interface MainUiEvent : BasicUiEvent {
    object GoToAllPizzaScreen : MainUiEvent
    object GoToNewOrdersScreen : MainUiEvent
    object GoToCompletedOrdersScreen : MainUiEvent
    object GoBack : MainUiEvent
    class GoToEditPizzaScreen(val pizzaId: String?) : MainUiEvent
}

sealed interface MainUiIntent : BasicUiIntent {
    object GoToAllPizzaScreenIntent : MainUiIntent
    object GoToNewOrdersScreenIntent : MainUiIntent
    object GoToCompletedOrdersScreenIntent : MainUiIntent
    object GoBackIntent : MainUiIntent
    class GoToEditPizzaScreen(val pizzaId: String?) : MainUiIntent
}