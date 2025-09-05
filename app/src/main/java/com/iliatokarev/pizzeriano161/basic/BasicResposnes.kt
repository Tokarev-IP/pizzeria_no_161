package com.iliatokarev.pizzeriano161.basic

sealed interface BasicFunctionResponse {
    object Completed: BasicFunctionResponse
    object Failed: BasicFunctionResponse
}