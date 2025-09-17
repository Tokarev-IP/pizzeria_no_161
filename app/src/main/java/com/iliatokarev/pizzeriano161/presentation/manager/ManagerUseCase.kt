package com.iliatokarev.pizzeriano161.presentation.manager

import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.data.FirebaseMainData
import com.iliatokarev.pizzeriano161.domain.auth.AuthUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.main.MainDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagerUseCase(
    private val authUseCase: AuthUseCaseInterface,
    private val mainDataUseCase: MainDataUseCaseInterface,
) : ManagerUseCaseInterface {

    override suspend fun signInUser(): BasicFunctionResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                authUseCase.registerUser()
            }
        }.fold(
            onSuccess = { BasicFunctionResponse.Completed },
            onFailure = { BasicFunctionResponse.Failed }
        )
    }

    override suspend fun getMainData(): MainDataResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                val isOpen = mainDataUseCase.getMainData() ?: return@withContext MainDataResponse.Failed
                return@withContext MainDataResponse.Success(isOpen)
            }.getOrElse {
                MainDataResponse.Failed
            }
        }
    }

    override suspend fun changeMainData(isOpen: Boolean): BasicFunctionResponse {
        return withContext(Dispatchers.IO) {
            runCatching {
                mainDataUseCase.uploadMainData(
                    FirebaseMainData(open = !isOpen)
                )
            }
        }.fold(
            onSuccess = { BasicFunctionResponse.Completed },
            onFailure = { BasicFunctionResponse.Failed }
        )
    }

}

interface MainDataResponse {
    class Success(val isOpen: Boolean) : MainDataResponse
    object Failed : MainDataResponse
}

interface ManagerUseCaseInterface {
    suspend fun signInUser(): BasicFunctionResponse
    suspend fun getMainData(): MainDataResponse
    suspend fun changeMainData(isOpen: Boolean): BasicFunctionResponse
}