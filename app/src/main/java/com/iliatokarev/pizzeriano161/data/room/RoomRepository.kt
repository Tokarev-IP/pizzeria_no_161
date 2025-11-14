package com.iliatokarev.pizzeriano161.data.room

import com.iliatokarev.pizzeriano161.domain.rejection.RejectionData
import com.iliatokarev.pizzeriano161.domain.rejection.toRejectionReasonsRoom
import kotlinx.coroutines.flow.Flow

class RoomRepository(
    private val roomDAO: RoomDAO,
) : RoomRepositoryInterface {

    override suspend fun getAllReasonsList(): List<RejectionReasonsRoom> {
        return roomDAO.getAll()
    }

    override suspend fun getAllReasonsFlow(): Flow<List<RejectionReasonsRoom>> {
        return roomDAO.getAllFlow()
    }

    override suspend fun addReason(reason: RejectionData) {
        return roomDAO.upsert(reason.toRejectionReasonsRoom())
    }

    override suspend fun deleteReason(id: Int) {
        return roomDAO.delete(id = id)
    }
}

interface RoomRepositoryInterface {
    suspend fun getAllReasonsList(): List<RejectionReasonsRoom>
    suspend fun getAllReasonsFlow(): Flow<List<RejectionReasonsRoom>>
    suspend fun addReason(reason: RejectionData)
    suspend fun deleteReason(id: Int)
}