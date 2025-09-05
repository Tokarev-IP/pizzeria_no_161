package com.iliatokarev.pizzeriano161.data

import com.iliatokarev.pizzeriano161.basic.TimeUtilsForOrder
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class FirebasePizzaData(
    @SerialName("id")
    val id: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("price")
    val price: Float = 0F,
    @SerialName("description")
    val description: String = "",
    @SerialName("photo_uri")
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
    @SerialName("id")
    val id: String,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("sum")
    val sum: Float,
    @SerialName("consumer_name")
    val consumerName: String,
    @SerialName("consumer_email")
    val consumerEmail: String,
    @SerialName("consumer_phone")
    val consumerPhone: String,
    @SerialName("pizza_list")
    val pizzaList: List<String>,
    @SerialName("additional_info")
    val additionalInfo: String,
    @SerialName("time")
    val time: Long,
)

fun FirebaseOrderData.toOrderData() = OrderData(
    id = this.id,
    isCompleted = this.isCompleted,
    sum = this.sum,
    consumerName = this.consumerName,
    consumerEmail = this.consumerEmail,
    consumerPhone = this.consumerPhone,
    pizzaList = this.pizzaList,
    additionalInfo = this.additionalInfo,
    time = TimeUtilsForOrder.longToString(this.time),
)