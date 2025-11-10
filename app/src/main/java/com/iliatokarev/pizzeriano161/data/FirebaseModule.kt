package com.iliatokarev.pizzeriano161.data

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val firebaseModule = module {
    factoryOf(::FirebaseFirestoreOrder) { bind<FirebaseFirestoreOrderInterface>() }
    factoryOf(::FirebaseFirestorePizza) { bind<FirebaseFirestorePizzaInterface>() }
    factoryOf(::FirebaseStorage) { bind<FirebaseStorageInterface>() }
    factoryOf(::FirebaseAuth) { bind<FirebaseAuthInterface>() }
    factoryOf(::FirebaseFirestoreEmail) { bind<FirebaseFirestoreEmailInterface>() }
    factoryOf(::FirebaseFirestoreMain) { bind<FirebaseFirestoreMainInterface>() }
    factoryOf(::FirebaseFirestoreOven) { bind<FirebaseFirestoreOvenInterface>() }
}