package com.iliatokarev.pizzeriano161.presentation.orders.common

import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.order.OrderDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrdersUseCase(
    private val orderDataUseCase: OrderDataUseCaseInterface,
) : OrdersUseCaseInterface {

    override suspend fun getCompletedOrders(): OrderDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                orderDataUseCase.getCompletedOrderDataList()
            }.fold(
                onSuccess = { data: List<OrderData> ->
                    OrderDataResponse.OrderDataSuccess(data)
                },
                onFailure = {
                    OrderDataResponse.OrderDataError
                }
            )
        }
    }

    override suspend fun getNewOrders(): OrderDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                orderDataUseCase.getNewOrderDataList()
            }.fold(
                onSuccess = { data: List<OrderData> ->
                    OrderDataResponse.OrderDataSuccess(data)
                },
                onFailure = {
                    OrderDataResponse.OrderDataError
                }
            )
        }
    }

    override suspend fun deleteOrder(
        orderId: String,
        orderDataList: List<OrderData>,
    ): OrderDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                orderDataUseCase.deleteOrderData(orderId)
                orderDataList.filter { order ->
                    order.id != orderId
                }
            }.fold(
                onSuccess = { data: List<OrderData> ->
                    OrderDataResponse.OrderDataSuccess(data)
                },
                onFailure = {
                    OrderDataResponse.OrderDataError
                }
            )
        }
    }

    override suspend fun updateOrderAsCompleted(
        orderData: OrderData,
        orderDataList: List<OrderData>,
    ): OrderDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                val newOrderData = orderData.copy(isCompleted = true)
                orderDataUseCase.uploadOrderData(newOrderData)
                orderDataList.map { order ->
                    if (order.id == newOrderData.id)
                        newOrderData
                    else
                        order
                }
            }.fold(
                onSuccess = { data: List<OrderData> ->
                    OrderDataResponse.OrderDataSuccess(data)
                },
                onFailure = {
                    OrderDataResponse.OrderDataError
                }
            )
        }
    }

    override suspend fun updateOrderAsNew(
        orderData: OrderData,
        orderDataList: List<OrderData>,
    ): OrderDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                val newOrderData = orderData.copy(isCompleted = false)
                orderDataUseCase.uploadOrderData(newOrderData)
                orderDataList.map { order ->
                    if (order.id == newOrderData.id)
                        newOrderData
                    else
                        order
                }
            }.fold(
                onSuccess = { data: List<OrderData> ->
                    OrderDataResponse.OrderDataSuccess(data)
                },
                onFailure = {
                    OrderDataResponse.OrderDataError
                }
            )
        }
    }

}

interface OrdersUseCaseInterface {
    suspend fun getCompletedOrders(): OrderDataResponse
    suspend fun getNewOrders(): OrderDataResponse
    suspend fun deleteOrder(
        orderId: String,
        orderDataList: List<OrderData>,
    ): OrderDataResponse

    suspend fun updateOrderAsCompleted(
        orderData: OrderData,
        orderDataList: List<OrderData>
    ): OrderDataResponse

    suspend fun updateOrderAsNew(
        orderData: OrderData,
        orderDataList: List<OrderData>,
    ): OrderDataResponse
}

sealed interface OrderDataResponse {
    class OrderDataSuccess(val orderDataList: List<OrderData>) : OrderDataResponse
    object OrderDataError : OrderDataResponse
}