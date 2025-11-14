package com.iliatokarev.pizzeriano161.data.firebase

class FirebaseFirestoreOrder() : FirebaseFirestoreOrderInterface, FirebaseFirestore() {
    override suspend fun uploadOrderData(
        data: FirebaseOrderData,
        collectionPath: String,
        documentName: String
    ) {
        return setDocumentData<FirebaseOrderData>(
            data = data,
            collectionPath = collectionPath,
            documentName = documentName
        )
    }

    override suspend fun downloadOrderData(
        collectionPath: String,
        documentName: String
    ): FirebaseOrderData? {
        return getDocumentData<FirebaseOrderData>(
            collectionPath = collectionPath,
            documentName = documentName
        )
    }

    override suspend fun downloadOrderDataList(
        collectionPath: String
    ): List<FirebaseOrderData> {
        return getCollectionData<FirebaseOrderData>(
            collectionPath = collectionPath
        )
    }

    override suspend fun downloadCompletedOrderDataList(
        collectionPath: String,
    ): List<FirebaseOrderData> {
        return getEqualCollectionDataDescending<FirebaseOrderData>(
            collectionPath = collectionPath,
            equalField = "completed",
            equalValue = true,
            orderBy = "time",
        )
    }

    override suspend fun downloadUncompletedOrderDataList(
        collectionPath: String,
    ): List<FirebaseOrderData> {
        return getEqualCollectionDataDescending<FirebaseOrderData>(
            collectionPath = collectionPath,
            equalField = "completed",
            equalValue = false,
            orderBy = "time",
        )
    }

    override suspend fun deleteOrderData(
        collectionPath: String,
        documentName: String
    ) {
        return deleteDocumentData(
            collectionPath = collectionPath,
            documentName = documentName
        )
    }
}

private const val ORDER_COLLECTION_PATH = "order"

interface FirebaseFirestoreOrderInterface {
    suspend fun uploadOrderData(
        data: FirebaseOrderData,
        collectionPath: String = ORDER_COLLECTION_PATH,
        documentName: String,
    )

    suspend fun downloadOrderData(
        collectionPath: String = ORDER_COLLECTION_PATH,
        documentName: String,
    ): FirebaseOrderData?

    suspend fun downloadOrderDataList(
        collectionPath: String = ORDER_COLLECTION_PATH,
    ): List<FirebaseOrderData>

    suspend fun downloadCompletedOrderDataList(
        collectionPath: String = ORDER_COLLECTION_PATH,
    ): List<FirebaseOrderData>

    suspend fun deleteOrderData(
        collectionPath: String = ORDER_COLLECTION_PATH,
        documentName: String,
    )

    suspend fun downloadUncompletedOrderDataList(
        collectionPath: String = ORDER_COLLECTION_PATH,
    ): List<FirebaseOrderData>
}