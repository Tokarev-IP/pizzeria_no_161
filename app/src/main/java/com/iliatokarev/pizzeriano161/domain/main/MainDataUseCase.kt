package com.iliatokarev.pizzeriano161.domain.main

import com.iliatokarev.pizzeriano161.data.firebase.FirebaseFirestoreMainInterface
import com.iliatokarev.pizzeriano161.data.firebase.FirebaseMainData

class MainDataUseCase(
    private val firebaseFirestoreMain: FirebaseFirestoreMainInterface,
) : MainDataUseCaseInterface {

    override suspend fun getMainData(): Boolean? {
        return firebaseFirestoreMain.getMainData().run {
            this?.open
        }
    }

    override suspend fun uploadMainData(mainData: FirebaseMainData) {
        return firebaseFirestoreMain.uploadMainData(mainData = mainData)
    }
}

interface MainDataUseCaseInterface {
    suspend fun getMainData(): Boolean?
    suspend fun uploadMainData(mainData: FirebaseMainData)
}