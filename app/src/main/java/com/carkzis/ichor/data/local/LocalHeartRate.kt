package com.carkzis.ichor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class LocalHeartRate(
    @PrimaryKey
    val pk: String,
    val date: String,
    val value: String
)

fun HeartRateDataPoint.toLocalHeartRate() = LocalHeartRate(
    pk = UUID.randomUUID().toString(),
    date = LocalDateTime.now().toString(),
    value = this.value.asDouble().toString()
)

