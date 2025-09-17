package com.iliatokarev.pizzeriano161.presentation.manager

import androidx.lifecycle.viewModelScope
import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManagerViewModel(
    private val managerUseCase: ManagerUseCaseInterface,
) : BasicViewModel<ManagerUiState, ManagerUiEvent, ManagerUiIntent>(
    initialUiState = ManagerUiState(isInitialLoading = true)
) {
    private val isOpenFlow = MutableStateFlow<Boolean?>(null)
    private val isOpenFLowState = isOpenFlow.asStateFlow()

    private fun setIsOpen(isOpen: Boolean) {
        isOpenFlow.value = isOpen
    }

    fun getIsOpen() = isOpenFLowState

    override fun setEvent(event: ManagerUiEvent) {
        when (event) {
            is ManagerUiEvent.DoUserAuth -> {
                authUser()
                getIsOpenData()
            }

            is ManagerUiEvent.ChangeIsOpenState -> {
                isOpenFlow.value?.let { isOpen ->
                    changeIsOpenState(isOpen)
                }
            }
        }
    }

    private fun authUser() {
        viewModelScope.launch {
            setUiState(ManagerUiState(isInitialLoading = true))
            managerUseCase.signInUser().apply {
                when (this) {
                    is BasicFunctionResponse.Completed -> {
                        setUiState(ManagerUiState())
                    }

                    is BasicFunctionResponse.Failed -> {
                        setUiState(ManagerUiState(isInitialError = true))
                    }
                }
            }
        }
    }

    private fun getIsOpenData() {
        viewModelScope.launch {
            setUiState(ManagerUiState(isInitialLoading = true))
            managerUseCase.getMainData().apply {
                when (this) {
                    is MainDataResponse.Success -> {
                        setIsOpen(this.isOpen)
                        setUiState(ManagerUiState())
                    }

                    is MainDataResponse.Failed -> {
                        setUiState(ManagerUiState(isInitialError = true))
                    }
                }
            }
        }
    }

    private fun changeIsOpenState(isOpen: Boolean) {
        viewModelScope.launch {
            setUiState(ManagerUiState(isLoading = true))
            managerUseCase.changeMainData(isOpen).apply {
                when (this) {
                    is BasicFunctionResponse.Completed -> {
                        setIsOpen(!isOpen)
                        setUiState(ManagerUiState())
                    }

                    is BasicFunctionResponse.Failed -> {
                        setUiState(ManagerUiState(isError = true))
                    }
                }

            }
        }
    }
}

class ManagerUiState(
    val isInitialLoading: Boolean = false,
    val isInitialError: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) : BasicUiState

sealed interface ManagerUiEvent : BasicUiEvent {
    object DoUserAuth : ManagerUiEvent
    object ChangeIsOpenState : ManagerUiEvent
}

sealed interface ManagerUiIntent : BasicUiIntent