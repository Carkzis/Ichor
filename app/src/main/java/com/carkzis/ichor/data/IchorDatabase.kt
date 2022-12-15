package com.carkzis.ichor.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalHeartRate::class], version = 1, exportSchema = false)
abstract class IchorDatabase: RoomDatabase() {
    abstract fun heartRateDao(): HeartRateDao
}