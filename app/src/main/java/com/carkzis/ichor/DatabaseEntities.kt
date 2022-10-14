package com.carkzis.ichor

import androidx.room.Entity
import androidx.room.PrimaryKey
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.round

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

fun List<LocalHeartRate>.toDomainHeartRate() = this.map {
    DomainHeartRate(
        date = LocalDateTime.parse(it.date)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        value = try {
            round(it.value.toDouble() * 100) / 100
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
            0.0
        }
    )
}
