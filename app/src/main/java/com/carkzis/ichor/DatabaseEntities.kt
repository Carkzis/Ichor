package com.carkzis.ichor

import androidx.room.Entity
import androidx.room.PrimaryKey
import timber.log.Timber
import java.time.LocalDateTime
import java.util.*

@Entity
data class LocalHeartRate(
    @PrimaryKey
    val pk: String,
    val date: String,
    val value: String
)

data class DomainHeartRate(
    val date: String,
    val value: Double
)

fun HeartRateDataPoint.toLocalHeartRate() = LocalHeartRate(
    pk = UUID.randomUUID().toString(),
    date = LocalDateTime.now().toString(),
    value = this.value.asDouble().toString()
)

fun LocalHeartRate.toDomainHeartRate() = DomainHeartRate(
    date = this.date,
    value = try {
        this.value.toDouble()
    } catch (e: Exception) {
        Timber.e(e.localizedMessage)
        0.0
    }
)