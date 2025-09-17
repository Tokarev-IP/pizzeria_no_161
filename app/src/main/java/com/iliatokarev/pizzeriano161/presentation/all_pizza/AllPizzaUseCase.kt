package com.iliatokarev.pizzeriano161.presentation.all_pizza

import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllPizzaUseCase(
    private val pizzaDataUseCase: PizzaDataUseCaseInterface,
) : AllPizzaUseCaseInterface {

    override suspend fun getAllPizzaDataList(): AllPizzaDataListResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                val pizzaDataList = pizzaDataUseCase.getAllPizzaDataList()
                AllPizzaDataListResponse.AllPizzaDataList(pizzaDataList)
            }.getOrElse { exception ->
                AllPizzaDataListResponse.Failed
            }
        }
    }

    override suspend fun deletePizzaData(
        pizzaId: String,
        pizzaDataList: List<PizzaData>
    ): AllPizzaDataListResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                pizzaDataUseCase.deletePizzaData(pizzaId)
                val newPizzaList = pizzaDataList.filter { it.id != pizzaId }
                AllPizzaDataListResponse.AllPizzaDataList(newPizzaList)
            }.getOrElse {
                AllPizzaDataListResponse.Failed
            }
        }
    }

    override suspend fun changeAvailableStatePizzaData(
        pizzaData: PizzaData,
        pizzaDataList: List<PizzaData>
    ): AllPizzaDataListResponse {
        return withContext(Dispatchers.IO){
            runCatching {
                val newPizzaData = pizzaData.copy(isAvailable = !pizzaData.isAvailable)
                pizzaDataUseCase.uploadPizzaData(newPizzaData)
                pizzaDataList.map {
                    if (it.id == pizzaData.id)
                        newPizzaData
                    else
                        it
                }
            }.fold(
                onSuccess = { data: List<PizzaData> ->
                    AllPizzaDataListResponse.AllPizzaDataList(data)
                },
                onFailure = {
                    AllPizzaDataListResponse.Failed
                }
            )
        }
    }
}

interface AllPizzaUseCaseInterface {
    suspend fun getAllPizzaDataList(): AllPizzaDataListResponse
    suspend fun deletePizzaData(
        pizzaId: String,
        pizzaDataList: List<PizzaData>
    ): AllPizzaDataListResponse

    suspend fun changeAvailableStatePizzaData(
        pizzaData: PizzaData,
        pizzaDataList: List<PizzaData>
    ): AllPizzaDataListResponse
}

interface AllPizzaDataListResponse {
    class AllPizzaDataList(val pizzaDataList: List<PizzaData>) : AllPizzaDataListResponse
    object Failed : AllPizzaDataListResponse
}