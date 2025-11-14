package com.iliatokarev.pizzeriano161.domain.rejection

import com.iliatokarev.pizzeriano161.data.room.RoomRepositoryInterface
import com.iliatokarev.pizzeriano161.data.room.toRejectionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RejectionDataUseCase(
    private val roomRepository: RoomRepositoryInterface,
) : RejectionDataUseCaseInterface {

    override suspend fun addRejectionReason(reason: String) {
        roomRepository.addReason(
            RejectionData(
                id = INITIAL_ID,
                reason = reason,
            )
        )
    }

    override suspend fun deleteRejectionReason(id: Int) {
        roomRepository.deleteReason(id = id)
    }

    override suspend fun getAllRejectionReasons(): List<RejectionData> {
        return roomRepository.getAllReasonsList().map { rejectionReasonsRoom ->
            rejectionReasonsRoom.toRejectionData()
        }
    }

    override suspend fun getAllRejectionReasonsFlow(): Flow<List<RejectionData>> {
        return roomRepository.getAllReasonsFlow().map { rejectionReasonsRoomList ->
            rejectionReasonsRoomList.map { rejectionReasonsRoom ->
                rejectionReasonsRoom.toRejectionData()
            }
        }
    }
}

private const val INITIAL_ID = 0

interface RejectionDataUseCaseInterface {
    suspend fun addRejectionReason(reason: String)
    suspend fun deleteRejectionReason(id: Int)
    suspend fun getAllRejectionReasons(): List<RejectionData>
    suspend fun getAllRejectionReasonsFlow(): Flow<List<RejectionData>>
}
