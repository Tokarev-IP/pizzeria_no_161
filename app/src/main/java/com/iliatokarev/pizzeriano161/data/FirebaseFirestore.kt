package com.iliatokarev.pizzeriano161.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

open class FirebaseFirestore() {

    protected val firestore = Firebase.firestore

    protected suspend inline fun <reified T : Any> setDocumentData(
        data: T,
        collectionPath: String,
        documentName: String,
    ) {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(collectionPath).document(documentName).set(data)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    protected suspend inline fun <reified T : Any> getDocumentData(
        collectionPath: String,
        documentName: String,
    ): T? {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(collectionPath).document(documentName).get()
                .addOnSuccessListener { document ->
                    continuation.resume(document.toObject(T::class.java))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    protected suspend inline fun <reified T : Any> getCollectionData(
        collectionPath: String,
    ): List<T> {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(collectionPath)
                .get()
                .addOnSuccessListener { documents ->
                    val list = mutableListOf<T>()
                    for (document in documents) {
                        list.add(document.toObject(T::class.java))
                    }
                    continuation.resume(list)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    protected suspend inline fun <reified T : Any> getEqualCollectionDataDescending(
        collectionPath: String,
        equalField: String,
        equalValue: Any,
        orderBy:String,
    ): List<T> {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(collectionPath)
                .whereEqualTo(equalField, equalValue)
                .orderBy(orderBy, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    val list = mutableListOf<T>()
                    for (document in documents) {
                        list.add(document.toObject(T::class.java))
                    }
                    continuation.resume(list)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    protected suspend fun deleteDocumentData(
        collectionPath: String,
        documentName: String,
    ) {
        return suspendCancellableCoroutine { continuation ->
            firestore.collection(collectionPath).document(documentName).delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}