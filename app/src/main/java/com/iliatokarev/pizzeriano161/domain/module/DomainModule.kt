package com.iliatokarev.pizzeriano161.domain.module

import com.iliatokarev.pizzeriano161.domain.auth.AuthUseCase
import com.iliatokarev.pizzeriano161.domain.auth.AuthUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.main.MainDataUseCase
import com.iliatokarev.pizzeriano161.domain.main.MainDataUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.message.MessageDataUseCase
import com.iliatokarev.pizzeriano161.domain.message.MessageDataUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.order.OrderDataUseCase
import com.iliatokarev.pizzeriano161.domain.order.OrderDataUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.oven.OvenDataUseCase
import com.iliatokarev.pizzeriano161.domain.oven.OvenDataUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaDataUseCase
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaDataUseCaseInterface
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModules = module {
    factoryOf(::OrderDataUseCase) { bind<OrderDataUseCaseInterface>() }
    factoryOf(::PizzaDataUseCase) { bind<PizzaDataUseCaseInterface>() }
    factoryOf(::AuthUseCase) { bind<AuthUseCaseInterface>() }
    factoryOf(::MessageDataUseCase) { bind<MessageDataUseCaseInterface>() }
    factoryOf(::MainDataUseCase) { bind<MainDataUseCaseInterface>() }
    factoryOf(::OvenDataUseCase) { bind<OvenDataUseCaseInterface>() }
}