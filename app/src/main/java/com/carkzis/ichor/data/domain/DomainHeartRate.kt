package com.carkzis.ichor.data.domain

import com.carkzis.ichor.data.local.LocalHeartRate
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.round

data class DomainHeartRate(
    val pk: String,
    val date: String,
    val value: Double
)

fun List<LocalHeartRate>.toDomainHeartRate() = this.map {
    DomainHeartRate(
        pk = it.pk,
        date = try {
            LocalDateTime.parse(it.date)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        } catch (e: DateTimeParseException) {
            ""
        },
        value = try {
            round(it.value.toDouble() * 100) / 100
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
            0.0
        }
    )
}