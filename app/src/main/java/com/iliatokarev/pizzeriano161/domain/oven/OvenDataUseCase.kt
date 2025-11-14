package com.iliatokarev.pizzeriano161.domain.oven

import com.iliatokarev.pizzeriano161.data.firebase.FirebaseFirestoreOvenInterface
import com.iliatokarev.pizzeriano161.data.firebase.FirebaseOvenData
import kotlinx.coroutines.withTimeout

class OvenDataUseCase(
    private val firebaseFirestoreOven: FirebaseFirestoreOvenInterface,
): OvenDataUseCaseInterface {

    override suspend fun uploadOvenData(data: FirebaseOvenData) {
        withTimeout(TIMEOUT_TIME){
            firebaseFirestoreOven.uploadOvenState(ovenData = data)
        }
    }

    override suspend fun downloadOvenData(): Boolean? {
        return firebaseFirestoreOven.downloadOvenState().run {
            this?.hot
        }
    }

}

private const val TIMEOUT_TIME = 30000L

interface OvenDataUseCaseInterface {
    suspend fun uploadOvenData(
        data: FirebaseOvenData
    )

    suspend fun downloadOvenData(): Boolean?
}