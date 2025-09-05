package com.iliatokarev.pizzeriano161.domain.order

import com.iliatokarev.pizzeriano161.data.FirebaseFirestoreOrderInterface
import com.iliatokarev.pizzeriano161.data.toOrderData
import kotlinx.coroutines.withTimeout

class OrderDataUseCase(
    private val firebaseFirestoreOrder: FirebaseFirestoreOrderInterface,
) : OrderDataUseCaseInterface {

    override suspend fun getCompletedOrderDataList(): List<OrderData> {
        return firebaseFirestoreOrder.downloadCompletedOrderDataList().map { firebaseOrderData ->
            firebaseOrderData.toOrderData()
        }
    }

    override suspend fun getNewOrderDataList(): List<OrderData> {
        return firebaseFirestoreOrder.downloadUncompletedOrderDataList().map { firebaseOrderData ->
            firebaseOrderData.toOrderData()
        }
    }

    override suspend fun uploadOrderData(orderData: OrderData) {
        withTimeout(TIMEOUT_TIME) {
            firebaseFirestoreOrder.uploadOrderData(
                data = orderData.toFirebaseOrderData(),
                documentName = orderData.id,
            )
        }
    }

    override suspend fun deleteOrderData(orderId: String) {
        withTimeout(TIMEOUT_TIME) {
            firebaseFirestoreOrder.deleteOrderData(
                documentName = orderId,
            )
        }
    }
}

interface OrderDataUseCaseInterface {
    suspend fun getCompletedOrderDataList(): List<OrderData>
    suspend fun getNewOrderDataList(): List<OrderData>
    suspend fun uploadOrderData(orderData: OrderData)
    suspend fun deleteOrderData(orderId: String)
}

private const val TIMEOUT_TIME = 15000L