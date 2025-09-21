package com.iliatokarev.pizzeriano161.basic

fun String.toTelLink(): String {
    val newTel = this.filter { char ->
        char !in listOf<Char>('_', '(', ')', ' ', '-')
    }
    return "tel:${newTel}"
}

fun String.toEmailLink(): String {
    return "mailto:${this}"
}