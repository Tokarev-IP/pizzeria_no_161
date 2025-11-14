package com.iliatokarev.pizzeriano161.data.firebase

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseStorage : FirebaseStorageInterface {
    private val firebaseStorageBase = FirebaseStorageBase()

    override suspend fun uploadPhoto(fileUri: Uri, name: String) {
        firebaseStorageBase.uploadFile(
            fileUri = fileUri,
            name = "$name.jpeg",
        )
    }

    override suspend fun downloadPhotoUri(name: String): Uri {
        return firebaseStorageBase.downloadUrl(name = "$name${IMAGE_SIZE}.jpeg")
    }

    override suspend fun deletePhoto(name: String) {
        firebaseStorageBase.deleteFile(name = "$name.jpeg")
    }

    override suspend fun deleteCompressedPhoto(name: String) {
        firebaseStorageBase.deleteFile(name = "$name${IMAGE_SIZE}.jpeg")
    }


    private class FirebaseStorageBase {
        private val storage = Firebase.storage

        suspend fun uploadFile(
            fileUri: Uri,
            name: String,
        ) {
            return suspendCancellableCoroutine { continuation ->
                storage.reference.child(name).putFile(fileUri)
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }

        suspend fun downloadUrl(name: String): Uri {
            return suspendCancellableCoroutine { continuation ->
                storage.reference.child(name).downloadUrl
                    .addOnCompleteListener { task ->
                        continuation.resume(task.result)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }

        suspend fun deleteFile(name: String) {
            return suspendCancellableCoroutine { continuation ->
                storage.reference.child(name).delete()
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }
}

interface FirebaseStorageInterface {
    suspend fun uploadPhoto(fileUri: Uri, name: String)
    suspend fun downloadPhotoUri(name: String): Uri
    suspend fun deletePhoto(name: String)
    suspend fun deleteCompressedPhoto(name: String)
}

private const val IMAGE_SIZE = "_1000x1000"