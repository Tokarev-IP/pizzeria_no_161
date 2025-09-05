package com.iliatokarev.pizzeriano161.domain.pizza

import com.iliatokarev.pizzeriano161.data.FirebaseFirestorePizzaInterface
import com.iliatokarev.pizzeriano161.data.FirebaseStorageInterface
import com.iliatokarev.pizzeriano161.data.toPizzaData
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

class PizzaDataUseCase(
    private val firebaseFirestorePizza: FirebaseFirestorePizzaInterface,
    private val firebaseStorage: FirebaseStorageInterface,
) : PizzaDataUseCaseInterface {

    override suspend fun getAllPizzaDataList(): List<PizzaData> {
        return firebaseFirestorePizza.downloadPizzaDataList().map { firebasePizzaData ->
            firebasePizzaData.toPizzaData()
        }
    }

    override suspend fun getPizzaData(pizzaId: String): PizzaData? {
        return firebaseFirestorePizza.downloadPizzaData(documentName = pizzaId)?.toPizzaData()
    }

    override suspend fun uploadPizzaData(pizzaData: PizzaData) {
        withTimeout(TIMEOUT_TIME) {
            var newPizzaData = pizzaData
            if (pizzaData.photoUriFirebase == null && pizzaData.photoUri != null) {
                firebaseStorage.uploadPhoto(
                    fileUri = pizzaData.photoUri,
                    name = pizzaData.id,
                )
                delay(DELAY_TIME)
                val newUri = firebaseStorage.downloadPhotoUri(
                    name = pizzaData.id
                )
                newPizzaData = newPizzaData.copy(photoUriFirebase = newUri.toString())
            }
            firebaseFirestorePizza.uploadPizzaData(
                data = newPizzaData.toFirebasePizzaData(),
                documentName = pizzaData.id
            )
        }
    }

    override suspend fun deletePizzaData(pizzaId: String) {
        withTimeout(TIMEOUT_TIME) {
            delay(SHORT_DELAY)
            firebaseStorage.deletePhoto(name = pizzaId)
            firebaseStorage.deleteCompressedPhoto(name = pizzaId)
            firebaseFirestorePizza.deletePizzaData(documentName = pizzaId)
        }
    }
}

interface PizzaDataUseCaseInterface {
    suspend fun getAllPizzaDataList(): List<PizzaData>
    suspend fun getPizzaData(pizzaId: String): PizzaData?
    suspend fun uploadPizzaData(pizzaData: PizzaData)
    suspend fun deletePizzaData(pizzaId: String)
}

private const val SHORT_DELAY = 1000L
private const val DELAY_TIME = 10000L
private const val TIMEOUT_TIME = 30000L