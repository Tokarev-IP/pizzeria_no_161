package com.iliatokarev.pizzeriano161.presentation.manager

import androidx.lifecycle.viewModelScope
import com.iliatokarev.pizzeriano161.basic.BasicFunResponseData
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

    private val isOvenHot = MutableStateFlow<Boolean?>(null)
    private val isOvenHotState = isOvenHot.asStateFlow()

    private fun setIsOpen(isOpen: Boolean) {
        isOpenFlow.value = isOpen
    }

    private fun setIsOvenHot(isHot: Boolean) {
        isOvenHot.value = isHot
    }

    fun getIsOpen() = isOpenFLowState
    fun getIsOvenHot() = isOvenHotState

    override fun setEvent(event: ManagerUiEvent) {
        when (event) {
            is ManagerUiEvent.DoInitialActions -> {
                doUserAuthAndData()
            }

            is ManagerUiEvent.ChangeIsOpenState -> {
                isOpenFlow.value?.let { isOpen ->
                    changeIsOpenState(isOpen)
                }
            }

            is ManagerUiEvent.ChangeIsOvenHotState -> {
                isOvenHot.value?.let { isHot ->
                    changeOvenData(!isHot)
                }
            }
        }
    }

    private fun doUserAuthAndData() {
        viewModelScope.launch {
            authUser()
            getIsOpenData()
            getIsOvenHotData()
        }
    }

    private suspend fun authUser() {
        setUiState(ManagerUiState(isInitialLoading = true))
        managerUseCase.signInUser().let {
            when (it) {
                is BasicFunctionResponse.Completed -> {
                    setUiState(ManagerUiState())
                }

                is BasicFunctionResponse.Failed -> {
                    setUiState(ManagerUiState(isInitialError = true))
                }
            }
        }
    }

    private suspend fun getIsOpenData() {
        setUiState(ManagerUiState(isLoading = true))
        managerUseCase.getMainData().let {
            when (it) {
                is BasicFunResponseData.CompletedData -> {
                    setIsOpen(it.data)
                    setUiState(ManagerUiState())
                }

                is BasicFunResponseData.Failed -> {
                    setUiState(ManagerUiState(isInitialError = true))
                }
            }
        }
    }

    private fun changeIsOpenState(isOpen: Boolean) {
        viewModelScope.launch {
            setUiState(ManagerUiState(isLoading = true))
            managerUseCase.changeMainData(isOpen).let {
                when (it) {
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

    private fun getIsOvenHotData() {
        viewModelScope.launch {
            setUiState(ManagerUiState(isLoading = true))
            managerUseCase.getOvenData().let {
                when (it) {
                    is BasicFunResponseData.CompletedData -> {
                        setIsOvenHot(it.data)
                        setUiState(ManagerUiState())
                    }

                    is BasicFunResponseData.Failed -> {
                        setUiState(ManagerUiState(isError = true))
                    }
                }
            }
        }
    }

    private fun changeOvenData(data: Boolean) {
        viewModelScope.launch {
            setUiState(ManagerUiState(isLoading = true))
            managerUseCase.changeOvenData(data).let {
                when (it) {
                    is BasicFunctionResponse.Completed -> {
                        setIsOvenHot(data)
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
    object DoInitialActions : ManagerUiEvent
    object ChangeIsOpenState : ManagerUiEvent
    object ChangeIsOvenHotState : ManagerUiEvent
}

sealed interface ManagerUiIntent : BasicUiIntent