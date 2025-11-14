package com.iliatokarev.pizzeriano161.data.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDAO {

    @Upsert
    suspend fun upsert(rejectionReasonsRoom: RejectionReasonsRoom)

    @Query("SELECT * FROM rejection_reasons")
    suspend fun getAll(): List<RejectionReasonsRoom>

    @Query("SELECT * FROM rejection_reasons")
    fun getAllFlow(): Flow<List<RejectionReasonsRoom>>

    @Query("DELETE FROM rejection_reasons WHERE id = :id")
    suspend fun delete(id: Int)
}