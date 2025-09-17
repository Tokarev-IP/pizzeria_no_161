package com.iliatokarev.pizzeriano161.presentation.all_pizza

import androidx.lifecycle.viewModelScope
import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllPizzaViewModel(
    private val allPizzaUseCase: AllPizzaUseCaseInterface
) : BasicViewModel<AllPizzaUiState, AllPizzaUiEvent, AllPizzaUiIntent>(
    initialUiState = AllPizzaUiState(isInitialLoading = true)
) {

    private val allPizzaDataList = MutableStateFlow<List<PizzaData>>(emptyList())
    private val allPizzaDataListFlow = allPizzaDataList.asStateFlow()

    private fun setAllPizzaDataList(pizzaDataList: List<PizzaData>) {
        allPizzaDataList.value = pizzaDataList
    }

    fun getAllPizzaDataList() = allPizzaDataListFlow

    override fun setEvent(event: AllPizzaUiEvent) {
        when (event) {
            is AllPizzaUiEvent.DownloadAllPizza -> {
                downloadAllPizzaData()
            }

            is AllPizzaUiEvent.DeletePizza -> {
                deletePizzaData(event.pizzaId, allPizzaDataList.value)
            }

            is AllPizzaUiEvent.ChangeAvailableState -> {
                val pizzaData = allPizzaDataList.value.find { it.id == event.pizzaId }
                if (pizzaData != null)
                    changeAvailableState(pizzaData, allPizzaDataList.value)
                else
                    setUiState(AllPizzaUiState(isError = true))
            }
        }
    }

    private fun downloadAllPizzaData() {
        viewModelScope.launch {
            setUiState(AllPizzaUiState(isInitialLoading = true))
            allPizzaUseCase.getAllPizzaDataList().apply {
                when (this) {
                    is AllPizzaDataListResponse.AllPizzaDataList -> {
                        setAllPizzaDataList(this.pizzaDataList)
                        setUiState(AllPizzaUiState())
                    }

                    is AllPizzaDataListResponse.Failed -> {
                        setUiState(AllPizzaUiState(isDownloadError = true))
                    }
                }
            }
        }
    }

    private fun deletePizzaData(pizzaId: String, pizzaDataList: List<PizzaData>) {
        viewModelScope.launch {
            setUiState(AllPizzaUiState(isLoading = true))
            allPizzaUseCase.deletePizzaData(pizzaId = pizzaId, pizzaDataList = pizzaDataList)
                .apply {
                    when (this) {
                        is AllPizzaDataListResponse.AllPizzaDataList -> {
                            setAllPizzaDataList(this.pizzaDataList)
                            setUiState(AllPizzaUiState())
                        }

                        is AllPizzaDataListResponse.Failed -> {
                            setUiState(AllPizzaUiState(isError = true))
                        }
                    }
                }
        }
    }

    private fun changeAvailableState(pizzaData: PizzaData, pizzaDataList: List<PizzaData>) {
        viewModelScope.launch {
            setUiState(AllPizzaUiState(isLoading = true))
            allPizzaUseCase.changeAvailableStatePizzaData(
                pizzaData = pizzaData,
                pizzaDataList = pizzaDataList,
            ).apply {
                when (this) {
                    is AllPizzaDataListResponse.AllPizzaDataList -> {
                        setAllPizzaDataList(this.pizzaDataList)
                        setUiState(AllPizzaUiState())
                    }

                    is AllPizzaDataListResponse.Failed -> {
                        setUiState(AllPizzaUiState(isError = true))
                    }
                }
            }
        }
    }
}

class AllPizzaUiState(
    val isInitialLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isDownloadError: Boolean = false,
    val isError: Boolean = false,
) : BasicUiState

sealed interface AllPizzaUiEvent : BasicUiEvent {
    object DownloadAllPizza : AllPizzaUiEvent
    class DeletePizza(val pizzaId: String) : AllPizzaUiEvent
    class ChangeAvailableState(val pizzaId: String) : AllPizzaUiEvent
}

interface AllPizzaUiIntent : BasicUiIntent {}