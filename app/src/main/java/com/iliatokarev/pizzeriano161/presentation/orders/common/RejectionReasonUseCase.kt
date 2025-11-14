package com.iliatokarev.pizzeriano161.presentation.orders.common

import com.iliatokarev.pizzeriano161.basic.BasicFunctionResponse
import com.iliatokarev.pizzeriano161.domain.rejection.RejectionData
import com.iliatokarev.pizzeriano161.domain.rejection.RejectionDataUseCaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext

class RejectionReasonUseCase(
    private val rejectionDataUseCase: RejectionDataUseCaseInterface,
) : RejectionReasonUseCaseInterface {

    override suspend fun getRejectionReasonList(): List<RejectionData> {
        return withContext(Dispatchers.IO) {
            runCatching {
                rejectionDataUseCase.getAllRejectionReasons()
            }.fold(
                onSuccess = { return@withContext it },
                onFailure = { return@withContext emptyList() }
            )
        }
    }

    override suspend fun getRejectionReasonListFlow(): Flow<List<RejectionData>> {
        return withContext(Dispatchers.IO) {
            rejectionDataUseCase.getAllRejectionReasonsFlow()
                .catch {
                    emit(emptyList())
                }
        }
    }

    override suspend fun addRejectionReason(reason: String): BasicFunctionResponse {
        return withContext(Dispatchers.IO){
            runCatching {
                rejectionDataUseCase.addRejectionReason(reason = reason)
            }.fold(
                onSuccess = { return@withContext BasicFunctionResponse.Completed },
                onFailure = { return@withContext BasicFunctionResponse.Failed }
            )
        }
    }

    override suspend fun deleteRejectionReason(id: Int): BasicFunctionResponse {
        return withContext(Dispatchers.IO){
            runCatching {
                rejectionDataUseCase.deleteRejectionReason(id = id)
            }.fold(
                onSuccess = { return@withContext BasicFunctionResponse.Completed },
                onFailure = { return@withContext BasicFunctionResponse.Failed }
            )
        }
    }

}

interface RejectionReasonUseCaseInterface {
    suspend fun getRejectionReasonList(): List<RejectionData>
    suspend fun getRejectionReasonListFlow(): Flow<List<RejectionData>>
    suspend fun addRejectionReason(reason: String): BasicFunctionResponse
    suspend fun deleteRejectionReason(id: Int): BasicFunctionResponse
}