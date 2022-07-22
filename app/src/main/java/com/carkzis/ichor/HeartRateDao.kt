package com.carkzis.ichor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(heartRates: LocalHeartRate)

    @Query("SELECT * FROM LocalHeartRate")
    fun getAllLocalHeartRates() : Flow<List<LocalHeartRate>>
}