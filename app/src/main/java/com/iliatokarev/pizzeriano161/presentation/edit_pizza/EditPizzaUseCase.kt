package com.iliatokarev.pizzeriano161.presentation.edit_pizza

import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditPizzaUseCase(
    private val pizzaDataUseCase: PizzaDataUseCaseInterface,
) : EditPizzaUseCaseInterface {

    override suspend fun downloadPizzaData(pizzaId: String): DownloadedPizzaDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                val pizzaData = pizzaDataUseCase.getPizzaData(pizzaId)
                pizzaData?.let {
                    DownloadedPizzaDataResponse.DownloadedDownloadedPizzaData(pizzaData)
                } ?: DownloadedPizzaDataResponse.Empty
            }.getOrElse {
                DownloadedPizzaDataResponse.Failed
            }
        }
    }

    override suspend fun uploadPizzaData(pizzaData: PizzaData): BasicFunctionResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                pizzaDataUseCase.uploadPizzaData(pizzaData)
            }
        }.fold(
            onSuccess = { BasicFunctionResponse.Completed },
            onFailure = { BasicFunctionResponse.Failed }
        )
    }
}

interface EditPizzaUseCaseInterface {
    suspend fun downloadPizzaData(pizzaId: String): DownloadedPizzaDataResponse
    suspend fun uploadPizzaData(pizzaData: PizzaData): BasicFunctionResponse
}

interface DownloadedPizzaDataResponse {
    class DownloadedDownloadedPizzaData(val pizzaData: PizzaData) : DownloadedPizzaDataResponse
    object Empty : DownloadedPizzaDataResponse
    object Failed : DownloadedPizzaDataResponse
}