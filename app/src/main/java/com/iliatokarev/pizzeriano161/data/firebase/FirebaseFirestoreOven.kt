package com.iliatokarev.pizzeriano161.data.firebase

class FirebaseFirestoreOven: FirebaseFirestoreOvenInterface, FirebaseFirestore() {

    override suspend fun downloadOvenState(
        collectionPath: String,
        documentName: String
    ): FirebaseOvenData? {
        return getDocumentData<FirebaseOvenData>(
            collectionPath = collectionPath,
            documentName = documentName,
        )
    }

    override suspend fun uploadOvenState(
        collectionPath: String,
        documentName: String,
        ovenData: FirebaseOvenData
    ) {
        setDocumentData(
            data = ovenData,
            collectionPath = collectionPath,
            documentName = documentName,
        )
    }
}

interface FirebaseFirestoreOvenInterface{

    suspend fun downloadOvenState(
        collectionPath: String = OVEN_COLLECTION_PATH,
        documentName: String = OVEN_DOCUMENT_NAME
    ): FirebaseOvenData?

    suspend fun uploadOvenState(
        collectionPath: String = OVEN_COLLECTION_PATH,
        documentName: String = OVEN_DOCUMENT_NAME,
        ovenData: FirebaseOvenData,
    )
}

private const val OVEN_COLLECTION_PATH = "oven"
private const val OVEN_DOCUMENT_NAME = "pizzeria-161"