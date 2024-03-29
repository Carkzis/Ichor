package com.carkzis.ichor.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeartRate(heartRate: LocalHeartRate)

    @Query("SELECT * FROM LocalHeartRate")
    fun getAllLocalHeartRates() : Flow<List<LocalHeartRate>>

    @Query("DELETE FROM LocalHeartRate WHERE pk = :primaryKey")
    fun deleteLocalHeartRate(primaryKey: String)

    @Query("DELETE FROM LocalHeartRate")
    fun deleteAllLocalHeartRates()
}