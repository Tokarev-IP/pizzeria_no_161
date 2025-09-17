package com.iliatokarev.pizzeriano161.presentation.main

import com.iliatokarev.pizzeriano161.presentation.all_pizza.AllPizzaUseCase
import com.iliatokarev.pizzeriano161.presentation.all_pizza.AllPizzaUseCaseInterface
import com.iliatokarev.pizzeriano161.presentation.all_pizza.AllPizzaViewModel
import com.iliatokarev.pizzeriano161.presentation.edit_order.EditOrderUseCase
import com.iliatokarev.pizzeriano161.presentation.edit_order.EditOrderUseCaseInterface
import com.iliatokarev.pizzeriano161.presentation.edit_order.EditOrderViewModel
import com.iliatokarev.pizzeriano161.presentation.edit_pizza.EditPizzaUseCase
import com.iliatokarev.pizzeriano161.presentation.edit_pizza.EditPizzaUseCaseInterface
import com.iliatokarev.pizzeriano161.presentation.edit_pizza.EditPizzaViewModel
import com.iliatokarev.pizzeriano161.presentation.manager.ManagerUseCase
import com.iliatokarev.pizzeriano161.presentation.manager.ManagerUseCaseInterface
import com.iliatokarev.pizzeriano161.presentation.manager.ManagerViewModel
import com.iliatokarev.pizzeriano161.presentation.orders.uncompleted.UncompletedOrdersViewModel
import com.iliatokarev.pizzeriano161.presentation.orders.common.OrdersUseCase
import com.iliatokarev.pizzeriano161.presentation.orders.common.OrdersUseCaseInterface
import com.iliatokarev.pizzeriano161.presentation.orders.completed.CompletedOrdersViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::UncompletedOrdersViewModel)
    viewModelOf(::CompletedOrdersViewModel)
    viewModelOf(::AllPizzaViewModel)
    viewModelOf(::EditPizzaViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ManagerViewModel)
    viewModelOf(::EditOrderViewModel)

    factoryOf(::AllPizzaUseCase) { bind<AllPizzaUseCaseInterface>() }
    factoryOf(::EditPizzaUseCase) { bind<EditPizzaUseCaseInterface>() }
    factoryOf(::ManagerUseCase) { bind<ManagerUseCaseInterface>() }
    factoryOf(::OrdersUseCase) { bind<OrdersUseCaseInterface>() }
    factoryOf(::EditOrderUseCase) { bind<EditOrderUseCaseInterface>() }
}