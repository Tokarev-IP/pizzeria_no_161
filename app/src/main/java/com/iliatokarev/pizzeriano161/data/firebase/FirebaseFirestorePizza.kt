package com.iliatokarev.pizzeriano161.data.firebase

class FirebaseFirestorePizza(): FirebaseFirestorePizzaInterface, FirebaseFirestore() {

    override suspend fun uploadPizzaData(
        data: FirebasePizzaData,
        collectionPath: String,
        documentName: String,
    ) {
        return setDocumentData<FirebasePizzaData>(
            data = data,
            collectionPath = collectionPath,
            documentName = documentName
        )
    }

    override suspend fun downloadPizzaData(
        collectionPath: String,
        documentName: String,
    ): FirebasePizzaData? {
        return getDocumentData<FirebasePizzaData>(
            collectionPath = collectionPath,
            documentName = documentName
        )
    }

    override suspend fun downloadPizzaDataList(
        collectionPath: String,
    ): List<FirebasePizzaData> {
        return getCollectionData<FirebasePizzaData>(
            collectionPath = collectionPath
        )
    }

    override suspend fun deletePizzaData(
        collectionPath: String,
        documentName: String,
    ) {
        return deleteDocumentData(
            collectionPath = collectionPath,
            documentName = documentName,
        )
    }
}

interface FirebaseFirestorePizzaInterface {
    suspend fun uploadPizzaData(
        data: FirebasePizzaData,
        collectionPath: String = PIZZA_COLLECTION_PATH,
        documentName: String,
    )

    suspend fun downloadPizzaData(
        collectionPath: String = PIZZA_COLLECTION_PATH,
        documentName: String,
    ): FirebasePizzaData?

    suspend fun downloadPizzaDataList(
        collectionPath: String = PIZZA_COLLECTION_PATH,
    ): List<FirebasePizzaData>

    suspend fun deletePizzaData(
        collectionPath: String = PIZZA_COLLECTION_PATH,
        documentName: String,
    )
}

private const val PIZZA_COLLECTION_PATH = "pizza"