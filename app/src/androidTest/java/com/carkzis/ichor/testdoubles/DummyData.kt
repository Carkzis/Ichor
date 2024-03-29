package com.carkzis.ichor.testdoubles

import androidx.health.services.client.data.*
import com.carkzis.ichor.data.local.LocalHeartRate
import java.time.Duration
import java.util.*

fun listOfHeartRateDataPoints() : List<List<DataPoint>> {
    val heartRateSamplesInDoubles = listOfHeartRateDataInDoubles()
    val heartRateDataPoints: MutableList<List<DataPoint>> = mutableListOf(listOf(), listOf(), listOf())

    heartRateSamplesInDoubles.forEachIndexed { index, heartRateSample ->
        heartRateSample.forEach { heartRate ->
            heartRateDataPoints[index] = heartRateDataPoints[index] + listOf(
                DataPoint.createSample(
                    DataType.HEART_RATE_BPM,
                    Value.ofDouble(heartRate),
                    Duration.ofSeconds(0)
                )
            )
        }
    }

    return heartRateDataPoints
}

fun listOfHeartRateDataAsMockDatabase() : List<LocalHeartRate> {
    return listOfHeartRateDataInDoubles().map {
        LocalHeartRate(
            date = "",
            pk = UUID.randomUUID().toString(),
            value = it.last().toString()
        )
    }
}

fun listOfHeartRateDataInDoubles() : List<List<Double>> {
    val expectedHeartRatesOne = listOf(45.0, 50.0, 55.0)
    val expectedHeartRatesTwo = listOf(30.0)
    val expectedHeartRatesThree = listOf(75.0, 100.0)
    return listOf(expectedHeartRatesOne, expectedHeartRatesTwo, expectedHeartRatesThree)
}

fun listOfAvailabilities() : List<Availability> {
    val expectedAvailability1 = DataTypeAvailability.UNKNOWN
    val expectedAvailability2 = DataTypeAvailability.ACQUIRING
    val expectedAvailability3 = DataTypeAvailability.AVAILABLE
    return listOf(
        expectedAvailability1, expectedAvailability2, expectedAvailability3
    )
}
