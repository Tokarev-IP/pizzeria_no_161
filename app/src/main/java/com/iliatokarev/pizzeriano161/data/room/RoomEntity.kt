package com.iliatokarev.pizzeriano161.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iliatokarev.pizzeriano161.domain.rejection.RejectionData

@Entity(tableName = "rejection_reasons")
data class RejectionReasonsRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reason: String,
)

fun RejectionReasonsRoom.toRejectionData() = RejectionData(
    id = this.id,
    reason = this.reason,
)


