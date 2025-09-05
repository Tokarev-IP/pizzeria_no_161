package com.iliatokarev.pizzeriano161.domain.order

import com.iliatokarev.pizzeriano161.basic.TimeUtilsForOrder
import com.iliatokarev.pizzeriano161.data.FirebaseOrderData

data class OrderData(
    val id: String,
    val isCompleted: Boolean,
    val sum: Float,
    val consumerName: String,
    val consumerPhone: String,
    val consumerEmail: String,
    val pizzaList: List<String>,
    val additionalInfo: String,
    val time: String,
)

fun OrderData.toFirebaseOrderData() = FirebaseOrderData(
    id = id,
    isCompleted = isCompleted,
    sum = sum,
    consumerName = consumerName,
    consumerEmail = consumerEmail,
    consumerPhone = consumerPhone,
    pizzaList = pizzaList,
    additionalInfo = additionalInfo,
    time = TimeUtilsForOrder.stringToLong(time),
)

val orderDataPreview = OrderData(
    id = "1234",
    isCompleted = true,
    sum = 5348F,
    consumerName = "Vasy",
    consumerEmail = "dconsumer@email.com",
    consumerPhone = "79482928483",
    pizzaList = listOf("4formagio", "salami", "classico pizzerio", "margarita"),
    additionalInfo = "good pizza for me",
    time = "14-42 25-09-2025",
)

val orderDataListPreview = listOf(
    OrderData(
        id = "1234",
        isCompleted = true,
        sum = 5348F,
        consumerName = "Vasy",
        consumerEmail = "dconsumer@email.com",
        consumerPhone = "79482928483",
        pizzaList = listOf("4formagio", "salami", "classico pizzerio", "margarita"),
        additionalInfo = "good pizza for me",
        time = "14-42 25-09-2025",
    ),
    OrderData(
        id = "8456",
        isCompleted = true,
        sum = 5348F,
        consumerName = "Vasy",
        consumerEmail = "dconsumer@email.com",
        consumerPhone = "79482928483",
        pizzaList = listOf("4formagio", "salami", "classico pizzerio", "margarita"),
        additionalInfo = "good pizza for me",
        time = "14-42 25-09-2025",
    ),
    OrderData(
        id = "87934",
        isCompleted = false,
        sum = 5348F,
        consumerName = "Vasy",
        consumerEmail = "dconsumer@email.com",
        consumerPhone = "79482928483",
        pizzaList = listOf("4formagio", "salami", "classico pizzerio", "margarita"),
        additionalInfo = "good pizza for me",
        time = "14-42 25-09-2025",
    ),
    OrderData(
        id = "2358",
        isCompleted = true,
        sum = 5348F,
        consumerName = "Vasy",
        consumerEmail = "dconsumer@email.com",
        consumerPhone = "79482928483",
        pizzaList = listOf("4formagio", "salami", "classico pizzerio", "margarita"),
        additionalInfo = "good pizza for me",
        time = "14-42 25-09-2025",
    ),
    OrderData(
        id = "8765",
        isCompleted = false,
        sum = 5348F,
        consumerName = "Vasy",
        consumerEmail = "dconsumer@email.com",
        consumerPhone = "79482928483",
        pizzaList = listOf("4formagio", "salami", "classico pizzerio", "margarita"),
        additionalInfo = "good pizza for me",
        time = "14-42 25-09-2025",
    )
)