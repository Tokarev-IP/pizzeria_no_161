package com.iliatokarev.pizzeriano161.domain.rejection

import com.iliatokarev.pizzeriano161.data.room.RejectionReasonsRoom

data class RejectionData(
    val id: Int,
    val reason: String,
)

fun RejectionData.toRejectionReasonsRoom() = RejectionReasonsRoom(
    id = this.id,
    reason = this.reason,
)