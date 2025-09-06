package com.iliatokarev.pizzeriano161.data

import com.iliatokarev.pizzeriano161.basic.TimeUtilsForOrder
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import kotlinx.serialization.Serializable

@Serializable
class FirebasePizzaData(
    val id: String = "",
    val name: String = "",
    val price: Float = 0F,
    val description: String = "",
    val photoUri: String? = null,
)

fun FirebasePizzaData.toPizzaData(): PizzaData {
    return PizzaData(
        id = this.id,
        name = this.name,
        price = this.price,
        description = this.description,
        photoUriFirebase = this.photoUri,
        photoUri = null,
    )
}

@Serializable
class FirebaseOrderData(
    val id: String = "",
    val completed: Boolean = false,
    val sum: Float = 0.0F,
    val consumerName: String = "",
    val consumerEmail: String = "",
    val consumerPhone: String = "",
    val pizzaList: List<String> = emptyList(),
    val additionalInfo: String = "",
    val time: Long = 0L,
)

fun FirebaseOrderData.toOrderData() = OrderData(
    id = this.id,
    isCompleted = this.completed,
    sum = this.sum,
    consumerName = this.consumerName,
    consumerEmail = this.consumerEmail,
    consumerPhone = this.consumerPhone,
    pizzaList = this.pizzaList,
    additionalInfo = this.additionalInfo,
    time = TimeUtilsForOrder.longToString(this.time),
)