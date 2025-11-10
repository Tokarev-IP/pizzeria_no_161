package com.iliatokarev.pizzeriano161.basic

sealed interface BasicFunctionResponse {
    object Completed: BasicFunctionResponse
    object Failed: BasicFunctionResponse
}

sealed interface BasicFunResponseData<out T>{
    class CompletedData<out T>(val data: T): BasicFunResponseData<T>
    object Failed: BasicFunResponseData<Nothing>
}