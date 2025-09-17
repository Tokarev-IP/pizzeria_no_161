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
                val orderData = uncompletedOrdersList.value.find { it.id == event.orderId }
                if (orderData != null) {
                    markOrdersAsCompleted(
                        orderData = orderData,
                        orderDataList = uncompletedOrdersList.value,
                    )
                } else
                    setUiState(UncompletedOrdersUiState(isError = true))
            }

            is UncompletedOrdersUiEvent.MarkOrderAsConfirmed -> {
                val orderData = uncompletedOrdersList.value.find { it.id == event.orderId }
                if (orderData != null) {
                    markOrderAsConfirmed(
                        orderData = orderData,
                        orderDataList = uncompletedOrdersList.value,
                    )
                } else
                    setUiState(UncompletedOrdersUiState(isError = true))
            }

            is UncompletedOrdersUiEvent.MarkOrderAsRejected -> {
                val orderData = uncompletedOrdersList.value.find { it.id == event.orderId }
                if (orderData != null) {
                    markOrderAsRejected(
                        orderData = orderData,
                        orderDataList = uncompletedOrdersList.value,
                        rejectionReason = event.rejectedReason,
                    )
                }
            }
        }
    }

    private fun getUncompletedOrders() {
        viewModelScope.launch {
            setUiState(UncompletedOrdersUiState(isInitialLoading = true))
            ordersUseCase.getNewOrders().apply {
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
                        setUiState(UncompletedOrdersUiState(isError = true))
                    }
                }
            }
        }
    }

    private fun markOrderAsConfirmed(orderData: OrderData, orderDataList: List<OrderData>) {
        viewModelScope.launch {
            setUiState(UncompletedOrdersUiState(isLoading = true))
            ordersUseCase.updateOrderAsConfirmed(
                orderData = orderData,
                orderDataList = orderDataList,
            ).apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setUncompletedOrdersList(this.orderDataList)
                        setUiState(UncompletedOrdersUiState())
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(UncompletedOrdersUiState(isError = true))
                    }
                }
            }
        }
    }

    private fun markOrderAsRejected(
        orderData: OrderData,
        orderDataList: List<OrderData>,
        rejectionReason: String,
    ) {
        viewModelScope.launch {
            setUiState(UncompletedOrdersUiState(isLoading = true))
            ordersUseCase.updateOrderAsRejected(
                orderData = orderData,
                orderDataList = orderDataList,
                rejectionReason = rejectionReason,
            ).apply {
                when (this) {
                    is OrderDataResponse.OrderDataSuccess -> {
                        setUncompletedOrdersList(this.orderDataList)
                        setUiState(UncompletedOrdersUiState())
                    }

                    is OrderDataResponse.OrderDataError -> {
                        setUiState(UncompletedOrdersUiState(isError = true))
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
    val isError: Boolean = false,
    val isDeleteOrderError: Boolean = false,
) : BasicUiState

sealed interface UncompletedOrdersUiEvent : BasicUiEvent {
    object LoadUncompletedOrders : UncompletedOrdersUiEvent
    class MarkOrdersAsCompleted(val orderId: String) : UncompletedOrdersUiEvent
    class DeleteOrder(val orderId: String) : UncompletedOrdersUiEvent
    class MarkOrderAsConfirmed(val orderId: String) : UncompletedOrdersUiEvent
    class MarkOrderAsRejected(val orderId: String, val rejectedReason: String) :
        UncompletedOrdersUiEvent
}

interface UncompletedOrdersUiIntent : BasicUiIntent