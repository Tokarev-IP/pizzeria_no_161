package com.iliatokarev.pizzeriano161.data.firebase

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
    val available: Boolean = true,
)

fun FirebasePizzaData.toPizzaData(): PizzaData {
    return PizzaData(
        id = this.id,
        name = this.name,
        price = this.price,
        description = this.description,
        photoUriFirebase = this.photoUri,
        photoUri = null,
        isAvailable = this.available,
    )
}

@Serializable
class FirebaseOrderData(
    val id: String = "",
    val completed: Boolean = false,
    val confirmed: Boolean = false,
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
    isConfirmed = this.confirmed,
    sum = this.sum,
    consumerName = this.consumerName,
    consumerEmail = this.consumerEmail,
    consumerPhone = this.consumerPhone,
    pizzaList = this.pizzaList,
    additionalInfo = this.additionalInfo,
    time = TimeUtilsForOrder.longToString(this.time),
    timeHour = TimeUtilsForOrder.longToHourString(this.time),
    timeDay = TimeUtilsForOrder.longToDateString(this.time),
)

@Serializable
class FirebaseMessageData(
    val to: List<String> = emptyList(),
    val message: MessageData = MessageData(),
)

@Serializable
class MessageData(
    val subject: String = "",
    val text: String = "",
    val html: String = "",
)

@Serializable
class FirebaseMainData(
    val open: Boolean = true,
)

@Serializable
class FirebaseOvenData(
    val hot: Boolean = false,
)