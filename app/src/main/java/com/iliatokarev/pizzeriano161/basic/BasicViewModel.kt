package com.iliatokarev.pizzeriano161.basic

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BasicViewModel<
        uiState : BasicUiState,
        uiEvent : BasicUiEvent,
        uiIntent : BasicUiIntent>(initialUiState: uiState) : ViewModel() {

    abstract fun setEvent(event: uiEvent)

    private val uiState = MutableStateFlow(initialUiState)
    private val uiStateFlow = uiState.asStateFlow()

    private val uiIntent = MutableSharedFlow<uiIntent>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    private val uiIntentFlow = uiIntent.asSharedFlow()

    protected fun setIntent(intent: uiIntent) {
        uiIntent.tryEmit(intent)
    }
    protected fun setUiState(state: uiState) {
        uiState.value = state
    }

    fun getUiState() = uiStateFlow
    fun getUiIntent() = uiIntentFlow
}

interface BasicUiState
interface BasicUiEvent
interface BasicUiIntent