package com.iliatokarev.pizzeriano161.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.iliatokarev.pizzeriano161.presentation.all_pizza.AllPizzaScreen
import com.iliatokarev.pizzeriano161.presentation.all_pizza.AllPizzaUiEvent
import com.iliatokarev.pizzeriano161.presentation.all_pizza.AllPizzaViewModel
import com.iliatokarev.pizzeriano161.presentation.edit_order.EditOrderScreen
import com.iliatokarev.pizzeriano161.presentation.edit_order.EditOrderUiEvent
import com.iliatokarev.pizzeriano161.presentation.edit_order.EditOrderViewModel
import com.iliatokarev.pizzeriano161.presentation.edit_pizza.EditPizzaScreen
import com.iliatokarev.pizzeriano161.presentation.edit_pizza.EditPizzaUiEvent
import com.iliatokarev.pizzeriano161.presentation.edit_pizza.EditPizzaViewModel
import com.iliatokarev.pizzeriano161.presentation.manager.ManagerScreen
import com.iliatokarev.pizzeriano161.presentation.manager.ManagerUiEvent
import com.iliatokarev.pizzeriano161.presentation.manager.ManagerViewModel
import com.iliatokarev.pizzeriano161.presentation.orders.completed.CompletedOrdersScreen
import com.iliatokarev.pizzeriano161.presentation.orders.completed.CompletedOrdersUiEvent
import com.iliatokarev.pizzeriano161.presentation.orders.completed.CompletedOrdersViewModel
import com.iliatokarev.pizzeriano161.presentation.orders.uncompleted.UncompletedOrdersScreen
import com.iliatokarev.pizzeriano161.presentation.orders.uncompleted.UncompletedOrdersUiEvent
import com.iliatokarev.pizzeriano161.presentation.orders.uncompleted.UncompletedOrdersViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainActivityCompose(
    mainViewModel: MainViewModel = koinViewModel(),
) {
    val backStack = rememberNavBackStack(ScreensNavigation.ManagerScreenNav)

    LaunchedEffect(key1 = Unit) {
        mainViewModel.getUiIntent().collect { intent ->
            when (intent) {
                is MainUiIntent.GoToAllPizzaScreenIntent -> {
                    backStack.add(ScreensNavigation.AllPizzaScreenNav)
                }

                is MainUiIntent.GoToNewOrdersScreenIntent -> {
                    backStack.add(ScreensNavigation.NewOrdersScreenNav)
                }

                is MainUiIntent.GoToCompletedOrdersScreenIntent -> {
                    backStack.add(ScreensNavigation.CompletedOrdersScreenNav)
                }

                is MainUiIntent.GoBackIntent -> {
                    backStack.removeLastOrNull()
                }

                is MainUiIntent.GoToEditPizzaScreen -> {
                    backStack.add(ScreensNavigation.EditPizzaScreenNav(intent.pizzaId))
                }

                is MainUiIntent.GoToEditOrderScreen -> {
                    backStack.add(ScreensNavigation.EditOrderScreenNav(intent.orderId))
                }
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<ScreensNavigation.ManagerScreenNav> {
                val managerViewModel: ManagerViewModel = koinViewModel()
                managerViewModel.setEvent(ManagerUiEvent.DoUserAuth)

                ManagerScreen(
                    mainViewModel = mainViewModel,
                    managerViewModel = managerViewModel,
                )
            }

            entry<ScreensNavigation.CompletedOrdersScreenNav> {
                val completedOrdersViewModel: CompletedOrdersViewModel = koinViewModel()
                completedOrdersViewModel.setEvent(CompletedOrdersUiEvent.LoadCompletedOrders)

                CompletedOrdersScreen(
                    mainViewModel = mainViewModel,
                    completedOrdersViewModel = completedOrdersViewModel,
                )
            }

            entry<ScreensNavigation.NewOrdersScreenNav> {
                val uncompletedOrdersViewModel: UncompletedOrdersViewModel = koinViewModel()
                uncompletedOrdersViewModel.setEvent(UncompletedOrdersUiEvent.LoadUncompletedOrders)

                UncompletedOrdersScreen(
                    mainViewModel = mainViewModel,
                    uncompletedOrdersViewModel = uncompletedOrdersViewModel,
                )
            }

            entry<ScreensNavigation.AllPizzaScreenNav> {
                val allPizzaViewModel: AllPizzaViewModel = koinViewModel()
                allPizzaViewModel.setEvent(AllPizzaUiEvent.DownloadAllPizza)

                AllPizzaScreen(
                    allPizzaViewModel = allPizzaViewModel,
                    mainViewModel = mainViewModel,
                )
            }

            entry<ScreensNavigation.EditPizzaScreenNav> { key ->
                val editPizzaViewModel: EditPizzaViewModel = koinViewModel()
                editPizzaViewModel.setEvent(EditPizzaUiEvent.DownloadEditPizza(pizzaId = key.pizzaId))

                EditPizzaScreen(
                    mainViewModel = mainViewModel,
                    editPizzaViewModel = editPizzaViewModel,
                    pizzaId = key.pizzaId,
                )
            }

            entry<ScreensNavigation.EditOrderScreenNav> { key ->
                val editOrderViewModel: EditOrderViewModel = koinViewModel()
                editOrderViewModel.setEvent(EditOrderUiEvent.DownloadAllData(orderId = key.orderId))

                EditOrderScreen(
                    mainViewModel = mainViewModel,
                    editOrderViewModel = editOrderViewModel,
                    orderId = key.orderId,
                )
            }
        },
    )
}

sealed interface ScreensNavigation : NavKey {
    @Serializable
    object ManagerScreenNav : ScreensNavigation

    @Serializable
    object NewOrdersScreenNav : ScreensNavigation

    @Serializable
    object CompletedOrdersScreenNav : ScreensNavigation

    @Serializable
    object AllPizzaScreenNav : ScreensNavigation

    @Serializable
    class EditPizzaScreenNav(val pizzaId: String?) : ScreensNavigation

    @Serializable
    class EditOrderScreenNav(val orderId: String) : ScreensNavigation
}