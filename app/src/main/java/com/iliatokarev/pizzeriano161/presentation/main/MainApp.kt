package com.iliatokarev.pizzeriano161.presentation.main

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import com.iliatokarev.pizzeriano161.data.firebaseModule
import com.iliatokarev.pizzeriano161.domain.module.domainModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApp)
            modules(mainModule, firebaseModule, domainModules)
        }

        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )

//        Firebase.initialize(context = this)
//        Firebase.appCheck.installAppCheckProviderFactory(
//            PlayIntegrityAppCheckProviderFactory.getInstance(),
//        )
    }
}