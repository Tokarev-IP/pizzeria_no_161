package com.iliatokarev.pizzeriano161.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuth : FirebaseAuthInterface {

    private val auth = Firebase.auth

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun authenticateAnonymousUser() {
        return suspendCancellableCoroutine { continuation ->
            auth.signInAnonymously()
                .addOnSuccessListener { result ->
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }
}

interface FirebaseAuthInterface {
    fun getCurrentUser(): FirebaseUser?
    suspend fun authenticateAnonymousUser()
}