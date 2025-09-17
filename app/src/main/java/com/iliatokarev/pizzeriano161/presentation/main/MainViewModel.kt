package com.iliatokarev.pizzeriano161.presentation.main

import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel
import com.iliatokarev.pizzeriano161.presentation.main.MainUiIntent.*

class MainViewModel(
) : BasicViewModel<MainUiState, MainUiEvent, MainUiIntent>(MainUiState.State()) {

    override fun setEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.GoToAllPizzaScreen -> {
                setIntent(GoToAllPizzaScreenIntent)
            }

            is MainUiEvent.GoToCompletedOrdersScreen -> {
                setIntent(GoToCompletedOrdersScreenIntent)
            }

            is MainUiEvent.GoToNewOrdersScreen -> {
                setIntent(GoToNewOrdersScreenIntent)
            }

            is MainUiEvent.GoBack -> {
                setIntent(GoBackIntent)
            }

            is MainUiEvent.GoToEditPizzaScreen -> {
                setIntent(GoToEditPizzaScreen(event.pizzaId))
            }

            is MainUiEvent.GoToEditOrderScreen -> {
                setIntent(GoToEditOrderScreen(event.orderId))
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
    class GoToEditOrderScreen(val orderId: String) : MainUiEvent
}

sealed interface MainUiIntent : BasicUiIntent {
    object GoToAllPizzaScreenIntent : MainUiIntent
    object GoToNewOrdersScreenIntent : MainUiIntent
    object GoToCompletedOrdersScreenIntent : MainUiIntent
    object GoBackIntent : MainUiIntent
    class GoToEditPizzaScreen(val pizzaId: String?) : MainUiIntent
    class GoToEditOrderScreen(val orderId: String) : MainUiIntent
}