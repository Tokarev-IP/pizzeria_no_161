package com.iliatokarev.pizzeriano161.domain.message

import com.iliatokarev.pizzeriano161.data.firebase.FirebaseFirestoreEmailInterface
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import kotlinx.coroutines.withTimeout

class MessageDataUseCase (
    private val firebaseFirestoreEmail: FirebaseFirestoreEmailInterface
): MessageDataUseCaseInterface {

    override suspend fun sendConfirmationEmail(orderData: OrderData) {
        withTimeout(TIMEOUT_TIME) {
            firebaseFirestoreEmail.uploadConfirmationMessageData(
                orderData = orderData,
            )
        }
    }

    override suspend fun sendRejectionEmail(
        orderData: OrderData,
        rejectionReason: String
    ) {
        withTimeout(TIMEOUT_TIME) {
            firebaseFirestoreEmail.uploadRejectionMessageData(
                orderData = orderData,
                rejectionReason = rejectionReason,
            )
        }
    }

}

private const val TIMEOUT_TIME = 15000L

interface MessageDataUseCaseInterface {
    suspend fun sendConfirmationEmail(orderData: OrderData)
    suspend fun sendRejectionEmail(orderData: OrderData, rejectionReason: String)
}