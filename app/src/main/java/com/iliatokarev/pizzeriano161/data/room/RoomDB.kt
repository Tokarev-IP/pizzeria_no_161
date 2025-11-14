package com.iliatokarev.pizzeriano161.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RejectionReasonsRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDAO
}