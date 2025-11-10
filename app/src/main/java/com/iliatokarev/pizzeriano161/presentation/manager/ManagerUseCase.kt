package com.iliatokarev.pizzeriano161.presentation.manager

import com.iliatokarev.pizzeriano161.basic.BasicFunResponseData
import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.data.FirebaseMainData
import com.iliatokarev.pizzeriano161.data.FirebaseOvenData
import com.iliatokarev.pizzeriano161.domain.auth.AuthUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.main.MainDataUseCaseInterface
import com.iliatokarev.pizzeriano161.domain.oven.OvenDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagerUseCase(
    private val authUseCase: AuthUseCaseInterface,
    private val mainDataUseCase: MainDataUseCaseInterface,
    private val ovenDataUseCase: OvenDataUseCaseInterface,
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

    override suspend fun getMainData(): BasicFunResponseData<Boolean> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val isOpen = mainDataUseCase.getMainData() ?: return@withContext BasicFunResponseData.Failed
                return@withContext BasicFunResponseData.CompletedData(isOpen)
            }.getOrElse {
                BasicFunResponseData.Failed
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

    override suspend fun getOvenData(): BasicFunResponseData<Boolean> {
        return withContext(Dispatchers.IO){
            runCatching {
                ovenDataUseCase.downloadOvenData()
            }
        }.fold(
            onSuccess = {
                if (it == null)
                    BasicFunResponseData.Failed
                else
                    BasicFunResponseData.CompletedData(it)
            },
            onFailure = { BasicFunResponseData.Failed }
        )
    }

    override suspend fun changeOvenData(data: Boolean): BasicFunctionResponse {
        return withContext(Dispatchers.IO){
            runCatching {
                ovenDataUseCase.uploadOvenData(FirebaseOvenData(data))
            }
        }.fold(
            onSuccess = { BasicFunctionResponse.Completed },
            onFailure = { BasicFunctionResponse.Failed },
        )
    }
}


interface ManagerUseCaseInterface {
    suspend fun signInUser(): BasicFunctionResponse
    suspend fun getMainData(): BasicFunResponseData<Boolean>
    suspend fun changeMainData(isOpen: Boolean): BasicFunctionResponse
    suspend fun getOvenData(): BasicFunResponseData<Boolean>
    suspend fun changeOvenData(data: Boolean): BasicFunctionResponse
}