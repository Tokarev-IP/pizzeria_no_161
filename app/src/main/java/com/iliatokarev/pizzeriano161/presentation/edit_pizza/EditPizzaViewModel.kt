package com.iliatokarev.pizzeriano161.presentation.edit_pizza

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class EditPizzaViewModel(
    private val editPizzaUseCase: EditPizzaUseCaseInterface,
) : BasicViewModel<EditPizzaUiState, EditPizzaUiEvent, EditPizzaUiIntent>(
    initialUiState = EditPizzaUiState(isInitialLoading = true)
) {

    private val pizzaDataVM = MutableStateFlow<PizzaData?>(null)
    private val pizzaDataFlow = pizzaDataVM.asStateFlow()

    private fun setPizzaDataVM(pizzaData: PizzaData) {
        pizzaDataVM.value = pizzaData
    }

    fun getPizzaDataFlow() = pizzaDataFlow

    override fun setEvent(event: EditPizzaUiEvent) {
        when (event) {
            is EditPizzaUiEvent.DownloadEditPizza -> {
                downloadPizzaData(event.pizzaId)
            }

            is EditPizzaUiEvent.SetImageUri -> {
                pizzaDataVM.value =
                    pizzaDataVM.value?.copy(photoUri = event.uri, photoUriFirebase = null)
                setUiState(EditPizzaUiState())
            }

            is EditPizzaUiEvent.SavePizzaData -> {
                pizzaDataVM.value = pizzaDataVM.value?.copy(
                    name = event.shortPizzaData.name,
                    price = event.shortPizzaData.price,
                    description = event.shortPizzaData.description
                )
                pizzaDataVM.value?.let { savePizzaData(it) }
            }

            is EditPizzaUiEvent.ClearImageUri -> {
                pizzaDataVM.value =
                    pizzaDataVM.value?.copy(photoUri = null, photoUriFirebase = null)
            }
        }
    }

    private fun downloadPizzaData(pizzaId: String?) {
        if (pizzaId == null) {
            pizzaDataVM.value = PizzaData(id = UUID.randomUUID().toString())
            setUiState(EditPizzaUiState())
        } else
            viewModelScope.launch {
                setUiState(EditPizzaUiState(isInitialLoading = true))
                editPizzaUseCase.downloadPizzaData(pizzaId).apply {
                    when (this) {
                        is DownloadedPizzaDataResponse.DownloadedDownloadedPizzaData -> {
                            setPizzaDataVM(this.pizzaData)
                            setUiState(EditPizzaUiState())
                        }

                        is DownloadedPizzaDataResponse.Failed -> {
                            setUiState(EditPizzaUiState(isDownloadError = true))
                        }

                        is DownloadedPizzaDataResponse.Empty -> {
                            setPizzaDataVM(PizzaData(id = UUID.randomUUID().toString()))
                            setUiState(EditPizzaUiState())
                        }
                    }
                }
            }

    }

    private fun savePizzaData(pizzaData: PizzaData) {
        viewModelScope.launch {
            setUiState(EditPizzaUiState(isLoading = true))
            editPizzaUseCase.uploadPizzaData(pizzaData = pizzaData).apply {
                when (this) {
                    is BasicFunctionResponse.Completed -> {
                        setUiState(EditPizzaUiState(isSaveSuccess = true))
                    }

                    is BasicFunctionResponse.Failed -> {
                        setUiState(EditPizzaUiState(isUploadError = true))
                    }
                }
            }
        }
    }
}

class EditPizzaUiState(
    val isInitialLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isUploadError: Boolean = false,
    val isDownloadError: Boolean = false,
    val isSaveSuccess: Boolean = false,
) : BasicUiState

sealed interface EditPizzaUiEvent : BasicUiEvent {
    class DownloadEditPizza(val pizzaId: String?) : EditPizzaUiEvent
    class SetImageUri(val uri: Uri?) : EditPizzaUiEvent
    object ClearImageUri : EditPizzaUiEvent
    class SavePizzaData(val shortPizzaData: ShortPizzaData) : EditPizzaUiEvent
}

interface EditPizzaUiIntent : BasicUiIntent {}

class ShortPizzaData(
    val name: String,
    val price: Float,
    val description: String,
)