package com.iliatokarev.pizzeriano161.domain.auth

import com.iliatokarev.pizzeriano161.data.FirebaseAuthInterface
import kotlinx.coroutines.withTimeout

class AuthUseCase(
    private val firebaseAuth: FirebaseAuthInterface,
) : AuthUseCaseInterface {

    override suspend fun registerUser() {
        withTimeout(TIME_OUT) {
            val user = firebaseAuth.getCurrentUser()
            if (user == null) {
                firebaseAuth.authenticateAnonymousUser()
            }
        }
    }
}

interface AuthUseCaseInterface {
    suspend fun registerUser()
}

private const val TIME_OUT = 10000L