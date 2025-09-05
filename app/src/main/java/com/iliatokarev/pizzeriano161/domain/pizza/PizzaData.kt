package com.iliatokarev.pizzeriano161.domain.pizza

import android.net.Uri
import com.iliatokarev.pizzeriano161.data.FirebasePizzaData

data class PizzaData(
    val id: String,
    val name: String = "",
    val price: Float = 0F,
    val description: String = "",
    val photoUriFirebase: String? = null,
    val photoUri: Uri? = null,
)

fun PizzaData.toFirebasePizzaData(): FirebasePizzaData {
    return FirebasePizzaData(
        id = this.id,
        name = this.name,
        price = this.price,
        description = this.description,
        photoUri = this.photoUriFirebase,
    )
}

val PizzaDataPreviewEmptyPhoto = PizzaData(
    id = "1234",
    name = "Pizza 1",
    price = 100F,
    description = "Pizza 1 description",
    photoUriFirebase = null,
    photoUri = null,
)

val PizzaDataPreviewWithPhoto = PizzaData(
    id = "1234",
    name = "Pizza 1",
    price = 100F,
    description = "Pizza 1 description",
    photoUriFirebase = "photoUriFirebase",
    photoUri = null,
)

val PizzaDataListPreview = listOf<PizzaData>(
    PizzaData(
        id = "1234",
        name = "Pizza 1",
        price = 100F,
        description = "Pizza 1 description",
        photoUriFirebase = null,
        photoUri = null,
    ),
    PizzaData(
        id = "12345",
        name = "Pizza 2",
        price = 200F,
        description = "Pizza 2 description about pizza more and more word to check it properly",
        photoUriFirebase = null,
        photoUri = null,
    ),
    PizzaData(
        id = "123456",
        name = "Pizza 3333 3333 3333 333 3333 3333 3333 333 333 333 333 333 33 3 3 333  33 ",
        price = 300F,
        description = "Pizza 3 description",
        photoUriFirebase = null,
        photoUri = null,
    ),
    PizzaData(
        id = "1234567",
        name = "Pizza 4",
        price = 400F,
        description = "Pizza 4 description",
        photoUriFirebase = null,
        photoUri = null,
    ),
    PizzaData(
        id = "1234567",
        name = "Pizza 5",
        price = 500F,
        description = "Pizza 5 description",
        photoUriFirebase = null,
        photoUri = null,
    ),
)