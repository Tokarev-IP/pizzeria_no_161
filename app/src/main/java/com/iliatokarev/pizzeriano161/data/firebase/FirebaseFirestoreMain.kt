package com.iliatokarev.pizzeriano161.data.firebase

class FirebaseFirestoreMain() : FirebaseFirestoreMainInterface, FirebaseFirestore() {

    override suspend fun getMainData(
        collectionPath: String,
        documentId: String,
    ): FirebaseMainData? {
        return getDocumentData<FirebaseMainData>(
            collectionPath = collectionPath,
            documentName = documentId,
        )
    }

    override suspend fun uploadMainData(
        collectionPath: String,
        documentId: String,
        mainData: FirebaseMainData
    ) {
        return setDocumentData<FirebaseMainData>(
            data = mainData,
            collectionPath = collectionPath,
            documentName = documentId,
        )
    }
}

interface FirebaseFirestoreMainInterface {
    suspend fun getMainData(
        collectionPath: String = MAIN_COLLECTION_PATH,
        documentId: String = MAIN_DOCUMENT_ID,
    ): FirebaseMainData?

    suspend fun uploadMainData(
        collectionPath: String = MAIN_COLLECTION_PATH,
        documentId: String = MAIN_DOCUMENT_ID,
        mainData: FirebaseMainData,
    )
}

private const val MAIN_COLLECTION_PATH = "main"
private const val MAIN_DOCUMENT_ID = "pizzeria-161"