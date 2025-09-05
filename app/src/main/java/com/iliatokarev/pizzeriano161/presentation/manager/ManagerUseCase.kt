package com.iliatokarev.pizzeriano161.presentation.manager

import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.domain.auth.AuthUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagerUseCase(
    private val authUseCase: AuthUseCaseInterface,
) : ManagerUseCaseInterface {

    override suspend fun signInUser(): BasicFunctionResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                authUseCase.registerUser()
            }.fold(
                onSuccess = { BasicFunctionResponse.Completed },
                onFailure = { BasicFunctionResponse.Failed }
            )
        }
    }

}

interface ManagerUseCaseInterface {
    suspend fun signInUser(): BasicFunctionResponse
}