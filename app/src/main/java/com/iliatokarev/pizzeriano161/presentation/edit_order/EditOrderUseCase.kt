package com.iliatokarev.pizzeriano161.presentation.edit_order

import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.order.OrderDataUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditOrderUseCase(
    private val orderDataUseCase: OrderDataUseCaseInterface,
    private val pizzaDataUseCase: PizzaDataUseCaseInterface,
) : EditOrderUseCaseInterface {

    override suspend fun uploadOrderData(orderData: OrderData): OrderDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                val newOrderData =
                    orderData.copy(time = "${orderData.timeHour} ${orderData.timeDay}")
                orderDataUseCase.uploadOrderData(newOrderData)
                OrderDataResponse.Completed(newOrderData)
            }.getOrElse {
                OrderDataResponse.Failed
            }
        }
    }

    override suspend fun downloadAllData(orderId: String): DownloadAllDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                val allPizzaData = pizzaDataUseCase.getAllPizzaDataList()
                val orderData = orderDataUseCase.getOrderDataById(orderId)
                if (orderData == null) {
                    DownloadAllDataResponse.Failed
                } else
                    DownloadAllDataResponse.Completed(
                        pizzaDataList = allPizzaData,
                        orderData = orderData
                    )
            }.getOrElse {
                DownloadAllDataResponse.Failed
            }
        }
    }

    override fun addPizzaToList(
        pizzaData: PizzaData,
        pizzaDataList: List<PizzaData>,
        orderData: OrderData
    ): OrderDataResponse {
        return runCatching {
            orderData.copy(
                pizzaList = orderData.pizzaList.plus(pizzaData.name).sorted(),
                sum = orderData.sum + pizzaData.price,
            )
        }.fold(
            onSuccess = { OrderDataResponse.Completed(it) },
            onFailure = { OrderDataResponse.Failed }
        )
    }

    override fun deletePizzaInList(
        pizzaName: String,
        pizzaDataList: List<PizzaData>,
        orderData: OrderData
    ): OrderDataResponse {
        val pizzaData =
            pizzaDataList.find { it.name == pizzaName } ?: return OrderDataResponse.Failed

        val pizzaPosition = orderData.pizzaList.indexOfFirst { it == pizzaName }
        if (pizzaPosition == -1) return OrderDataResponse.Failed

        val newPizzaList = orderData.pizzaList.toMutableList().apply {
            removeAt(pizzaPosition)
        }

        val newPizzaData = orderData.copy(
            pizzaList = newPizzaList,
            sum = orderData.sum - pizzaData.price,
        )
        return OrderDataResponse.Completed(newPizzaData)

    }
}

interface OrderDataResponse {
    class Completed(val orderData: OrderData) : OrderDataResponse
    object Failed : OrderDataResponse
}

interface DownloadAllDataResponse {
    class Completed(
        val pizzaDataList: List<PizzaData>,
        val orderData: OrderData
    ) : DownloadAllDataResponse

    object Failed : DownloadAllDataResponse
}

interface EditOrderUseCaseInterface {
    suspend fun uploadOrderData(orderData: OrderData): OrderDataResponse
    suspend fun downloadAllData(orderId: String): DownloadAllDataResponse
    fun addPizzaToList(
        pizzaData: PizzaData,
        pizzaDataList: List<PizzaData>,
        orderData: OrderData
    ): OrderDataResponse

    fun deletePizzaInList(
        pizzaName: String,
        pizzaDataList: List<PizzaData>,
        orderData: OrderData
    ): OrderDataResponse
}