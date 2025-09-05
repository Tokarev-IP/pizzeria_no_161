package com.iliatokarev.pizzeriano161.presentation.orders.uncompleted

import androidx.lifecycle.viewModelScope
import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.presentation.orders.common.OrderDataResponse
import com.iliatokarev.pizzeriano161.presentation.orders.common.OrdersUseCaseInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UncompletedOrdersViewModel(
    private val ordersUseCase: OrdersUseCaseInterface,
) : BasicViewModel<UncompletedOrdersUiState, UncompletedOrdersUiEvent, UncompletedOrdersUiIntent>(
    UncompletedOrdersUiState(isInitialLoading = true)
) {
    private val uncompletedOrdersList = MutableStateFlow<List<OrderData>>(emptyList())
    private val uncompletedOrdersListFlow = uncompletedOrdersList.asStateFlow()

    private fun setUncompletedOrdersList(orderDataList: List<OrderData>) {
        uncompletedOrdersList.value = orderDataList
    }

    fun getUncompletedOrdersList() = uncompletedOrdersListFlow

    override fun setEvent(event: UncompletedOrdersUiEvent) {
        when (event) {
            is UncompletedOrdersUiEvent.DeleteOrder -> {
                deleteOrderData(
                    orderId = event.orderId,
                    orderDataList = uncompletedOrdersList.value,
                )
            }

            is UncompletedOrdersUiEvent.LoadUncompletedOrders -> {
                getUncompletedOrders()
            }

            is UncompletedOrdersUiEvent.MarkOrdersAsCompleted -> {
                markOrdersAsCompleted(
                    orderData = event.orderData,
                    orderDataList = uncompletedOrdersList.value,
                )
            }
        }
    }

    private fun getUncompletedOrders() {
        viewModelScope.launch {
            setUiState(UncompletedOrdersUiState(isInitialLoading = true))
            ordersUseCase.getCompletedOrders().apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setUncompletedOrdersList(this.orderDataList)
                        setUiState(UncompletedOrdersUiState())
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(UncompletedOrdersUiState(isLoadingOrdersError = true))
                    }
                }
            }
        }
    }

    private fun deleteOrderData(orderId: String, orderDataList: List<OrderData>) {
        viewModelScope.launch {
            setUiState(UncompletedOrdersUiState(isLoading = true))
            ordersUseCase.deleteOrder(
                orderId = orderId,
                orderDataList = orderDataList,
            ).apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setUncompletedOrdersList(this.orderDataList)
                        setUiState(UncompletedOrdersUiState())
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(UncompletedOrdersUiState(isDeleteOrderError = true))
                    }
                }
            }
        }
    }

    private fun markOrdersAsCompleted(orderData: OrderData, orderDataList: List<OrderData>) {
        viewModelScope.launch {
            setUiState(UncompletedOrdersUiState(isLoading = true))
            ordersUseCase.updateOrderAsCompleted(
                orderData = orderData,
                orderDataList = orderDataList,
            ).apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setUncompletedOrdersList(this.orderDataList)
                        setUiState(UncompletedOrdersUiState())
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(UncompletedOrdersUiState(isMarkAsCompletedError = true))
                    }
                }
            }
        }
    }
}

class UncompletedOrdersUiState(
    val isInitialLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingOrdersError: Boolean = false,
    val isMarkAsCompletedError: Boolean = false,
    val isDeleteOrderError: Boolean = false,
) : BasicUiState

sealed interface UncompletedOrdersUiEvent : BasicUiEvent {
    object LoadUncompletedOrders : UncompletedOrdersUiEvent
    class MarkOrdersAsCompleted(val orderData: OrderData) : UncompletedOrdersUiEvent
    class DeleteOrder(val orderId: String) : UncompletedOrdersUiEvent
}

interface UncompletedOrdersUiIntent : BasicUiIntent