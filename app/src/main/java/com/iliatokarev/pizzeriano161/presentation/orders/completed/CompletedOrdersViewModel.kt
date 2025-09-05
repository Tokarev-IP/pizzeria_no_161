package com.iliatokarev.pizzeriano161.presentation.orders.completed

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

class CompletedOrdersViewModel(
    private val ordersUseCase: OrdersUseCaseInterface,
) : BasicViewModel<CompletedOrdersUiState, CompletedOrdersUiEvent, CompletedOrdersUiIntent>(
    CompletedOrdersUiState(isInitialLoading = true)
) {
    private val completedOrdersList = MutableStateFlow<List<OrderData>>(emptyList())
    private val completedOrdersListFlow = completedOrdersList.asStateFlow()

    private fun setCompletedOrdersList(orderDataList: List<OrderData>) {
        completedOrdersList.value = orderDataList
    }

    fun getCompletedOrdersList() = completedOrdersListFlow

    override fun setEvent(event: CompletedOrdersUiEvent) {
        when (event) {
            is CompletedOrdersUiEvent.LoadCompletedOrders -> {
                getCompletedOrders()
            }

            is CompletedOrdersUiEvent.MarkOrderAsNew -> {
                markOrderAsNew(
                    orderData = event.orderData,
                    orderDataList = completedOrdersList.value,
                )
            }

            is CompletedOrdersUiEvent.DeleteOrder -> {
                deleteOrderData(
                    orderId = event.orderId,
                    orderDataList = completedOrdersList.value,
                )
            }
        }
    }

    private fun getCompletedOrders() {
        viewModelScope.launch {
            setUiState(CompletedOrdersUiState(isInitialLoading = true))
            ordersUseCase.getCompletedOrders().apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setCompletedOrdersList(this.orderDataList)
                        setUiState(CompletedOrdersUiState())
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(CompletedOrdersUiState(isLoadingOrdersError = true))
                    }
                }
            }
        }
    }

    private fun markOrderAsNew(orderData: OrderData, orderDataList: List<OrderData>) {
        viewModelScope.launch {
            setUiState(CompletedOrdersUiState(isLoading = true))
            ordersUseCase.updateOrderAsNew(
                orderData = orderData,
                orderDataList = orderDataList,
            ).apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setCompletedOrdersList(this.orderDataList)
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(CompletedOrdersUiState(isMarkOrderAsNewError = true))
                    }
                }
            }
        }
    }

    private fun deleteOrderData(orderId: String, orderDataList: List<OrderData>) {
        viewModelScope.launch {
            setUiState(CompletedOrdersUiState(isLoading = true))
            ordersUseCase.deleteOrder(
                orderId = orderId,
                orderDataList = orderDataList,
            ).apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setCompletedOrdersList(this.orderDataList)
                        setUiState(CompletedOrdersUiState())
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(CompletedOrdersUiState(isDeleteOrderError = true))
                    }
                }
            }
        }
    }
}

class CompletedOrdersUiState(
    val isInitialLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingOrdersError: Boolean = false,
    val isMarkOrderAsNewError: Boolean = false,
    val isDeleteOrderError: Boolean = false,
) : BasicUiState

sealed interface CompletedOrdersUiEvent : BasicUiEvent {
    object LoadCompletedOrders : CompletedOrdersUiEvent
    class MarkOrderAsNew(val orderData: OrderData) : CompletedOrdersUiEvent
    class DeleteOrder(val orderId: String) : CompletedOrdersUiEvent
}

interface CompletedOrdersUiIntent : BasicUiIntent