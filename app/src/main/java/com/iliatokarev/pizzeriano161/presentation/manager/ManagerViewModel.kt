package com.iliatokarev.pizzeriano161.presentation.manager

import androidx.lifecycle.viewModelScope
import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.basic.BasicUiEvent
import com.iliatokarev.pizzeriano161.basic.BasicUiIntent
import com.iliatokarev.pizzeriano161.basic.BasicUiState
import com.iliatokarev.pizzeriano161.basic.BasicViewModel
import kotlinx.coroutines.launch

class ManagerViewModel(
    private val managerUseCase: ManagerUseCaseInterface,
) : BasicViewModel<ManagerUiState, ManagerUiEvent, ManagerUiIntent>(
    initialUiState = ManagerUiState(isInitialLoading = true)
) {

    override fun setEvent(event: ManagerUiEvent) {
        when (event){
            is ManagerUiEvent.DoAuthUser -> {
                authUser()
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
                        setUiState(ManagerUiState(isUserNull = true))
                    }
                }
            }
        }
    }
}

class ManagerUiState(
    val isInitialLoading: Boolean = false,
    val isUserNull: Boolean = false,
) : BasicUiState

sealed interface ManagerUiEvent : BasicUiEvent {
    object DoAuthUser : ManagerUiEvent
}

sealed interface ManagerUiIntent : BasicUiIntent