package com.iliatokarev.pizzeriano161.presentation.edit_order

import androidx.lifecycle.viewModelScope
import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditOrderViewModel(
    private val editOrderUseCase: EditOrderUseCaseInterface,
) : BasicViewModel<EditOrderUiState, EditOrderUiEvent, EditOrderUiIntent>(
    initialUiState = EditOrderUiState(isInitialLoading = true)
) {
    private val orderDataFlow = MutableStateFlow<OrderData?>(null)
    private val orderDataFlowState = orderDataFlow.asStateFlow()

    private val allPizzaFlow = MutableStateFlow<List<PizzaData>>(emptyList())
    private val allPizzaFlowState = allPizzaFlow.asStateFlow()

    private fun setOrderData(orderData: OrderData) {
        orderDataFlow.value = orderData
    }

    private fun setAllPizza(pizzaDataList: List<PizzaData>) {
        allPizzaFlow.value = pizzaDataList
    }

    fun getOrderDataFlow() = orderDataFlowState
    fun getAllPizzaFlow() = allPizzaFlowState

    override fun setEvent(event: EditOrderUiEvent) {
        when (event) {
            is EditOrderUiEvent.DownloadAllData -> {
                downloadAllData(event.orderId)
            }

            is EditOrderUiEvent.UploadOrderData -> {
                orderDataFlow.value?.let { order ->
                    uploadOrderData(
                        orderData = order,
                        email = event.email,
                        comment = event.comment,
                    )
                } ?: run {
                    setUiState(EditOrderUiState(isError = true))
                }
            }

            is EditOrderUiEvent.SetDate -> {
                orderDataFlow.value?.let { orderData ->
                    setOrderData(orderData.copy(timeDay = event.date))
                }
            }

            is EditOrderUiEvent.SetHour -> {
                orderDataFlow.value?.let { orderData ->
                    setOrderData(orderData.copy(timeHour = event.hour))
                }
            }

            is EditOrderUiEvent.AddPizza -> {
                orderDataFlow.value?.let { orderData ->
                    addPizza(
                        pizzaData = event.pizzaData,
                        pizzaDataList = allPizzaFlow.value,
                        orderData = orderData
                    )
                } ?: run {
                    setUiState(EditOrderUiState(isError = true))
                }
            }

            is EditOrderUiEvent.DeletePizza -> {
                orderDataFlow.value?.let { orderData ->
                    deletePizza(
                        pizzaName = event.pizzaName,
                        pizzaDataList = allPizzaFlow.value,
                        orderData = orderData
                    )
                } ?: run {
                    setUiState(EditOrderUiState(isError = true))
                }
            }
        }
    }

    private fun downloadAllData(orderId: String) {
        viewModelScope.launch {
            setUiState(EditOrderUiState(isInitialLoading = true))
            editOrderUseCase.downloadAllData(orderId).apply {
                when (this) {
                    is DownloadAllDataResponse.Completed -> {
                        setAllPizza(this.pizzaDataList)
                        setOrderData(this.orderData)
                        setUiState(EditOrderUiState())
                    }

                    is DownloadAllDataResponse.Failed -> {
                        setUiState(EditOrderUiState(isInitialLoadingError = true))
                    }
                }
            }
        }
    }

    private fun uploadOrderData(orderData: OrderData, email: String, comment: String) {
        viewModelScope.launch {
            setUiState(EditOrderUiState(isLoading = true))
            val newOrderData = orderData.copy(
                consumerEmail = email,
                additionalInfo = comment
            )
            editOrderUseCase.uploadOrderData(newOrderData).apply {
                when (this) {
                    is OrderDataResponse.Completed -> {
                        setOrderData(this.orderData)
                        setUiState(EditOrderUiState(isSaved = true))
                    }

                    is OrderDataResponse.Failed -> {
                        setUiState(EditOrderUiState(isError = true))
                    }
                }
            }
        }
    }

    private fun addPizza(
        pizzaData: PizzaData,
        pizzaDataList: List<PizzaData>,
        orderData: OrderData,
    ) {
        editOrderUseCase.addPizzaToList(
            pizzaData = pizzaData,
            pizzaDataList = pizzaDataList,
            orderData = orderData
        ).apply {
            when (this) {
                is OrderDataResponse.Completed -> {
                    setOrderData(this.orderData)
                }

                is OrderDataResponse.Failed -> {
                    setUiState(EditOrderUiState(isError = true))
                }
            }
        }
    }

    private fun deletePizza(
        pizzaName: String,
        pizzaDataList: List<PizzaData>,
        orderData: OrderData
    ) {
        editOrderUseCase.deletePizzaInList(
            pizzaName = pizzaName,
            pizzaDataList = pizzaDataList,
            orderData = orderData
        ).apply {
            when (this) {
                is OrderDataResponse.Completed -> {
                    setOrderData(this.orderData)
                }

                is OrderDataResponse.Failed -> {
                    setUiState(EditOrderUiState(isError = true))
                }
            }
        }
    }

}

class EditOrderUiState(
    val isInitialLoading: Boolean = false,
    val isInitialLoadingError: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSaved: Boolean = false,
) : BasicUiState

sealed interface EditOrderUiEvent : BasicUiEvent {
    class UploadOrderData(val email: String, val comment: String) : EditOrderUiEvent
    class DownloadAllData(val orderId: String) : EditOrderUiEvent
    class SetHour(val hour: String) : EditOrderUiEvent
    class SetDate(val date: String) : EditOrderUiEvent
    class AddPizza(val pizzaData: PizzaData) : EditOrderUiEvent
    class DeletePizza(val pizzaName: String) : EditOrderUiEvent
}

interface EditOrderUiIntent : BasicUiIntent {}