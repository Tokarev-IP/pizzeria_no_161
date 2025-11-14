package com.iliatokarev.pizzeriano161.data.room

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "pizza_database"
        ).build()
    }

    single { get<AppDatabase>().roomDao() }

    factoryOf(::RoomRepository) { bind<RoomRepositoryInterface>() }
}