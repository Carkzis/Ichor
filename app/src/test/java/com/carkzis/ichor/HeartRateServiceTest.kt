package com.carkzis.ichor

import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.Value
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger


@ExperimentalCoroutinesApi
class HeartRateServiceTest {

    var sut: HeartRateService? = null

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `heart rate service emits heart rate data points in given order`() = runBlocking {
        val expectedHeartRate1 = 50.0
        val expectedHeartRate2 = 55.0
        val expectedHeartRate3 = 45.0
        val expectedHeartRateDataPoints = listOf(
            DataPoint.createSample(DataType.HEART_RATE_BPM, Value.ofDouble(expectedHeartRate1), Duration.ofSeconds(0)),
            DataPoint.createSample(DataType.HEART_RATE_BPM, Value.ofDouble(expectedHeartRate2), Duration.ofSeconds(0)),
            DataPoint.createSample(DataType.HEART_RATE_BPM, Value.ofDouble(expectedHeartRate3), Duration.ofSeconds(0)),
        )

        sut = DummyHeartRateService().apply {
            mockHeartRateSample = expectedHeartRateDataPoints
        }

        val heartRateEmissionCounter = AtomicInteger(0)
        val availabilityCounter = AtomicInteger(0)

        sut?.run {
            retrieveHeartRate().collect {
                when (it) {
                    is MeasureClientData.HeartRateDataPoints -> {
                        val currentIndex = heartRateEmissionCounter.getAndIncrement()
                        val expectedHeartRate = expectedHeartRateDataPoints[currentIndex].value.asDouble()
                        val actualHeartRate = it.dataPoints.last().value.asDouble()
                        assertEquals(expectedHeartRate, actualHeartRate, 0.01)
                    }
                    is MeasureClientData.HeartRateAvailability -> {
                        availabilityCounter.incrementAndGet()
                    }
                }
            }
        }

        assertTrue("A heart rate was not emitted.", heartRateEmissionCounter.get() == expectedHeartRateDataPoints.size)
        assertTrue("An availability was not emitted.", availabilityCounter.get() == 0)
    }

    @Test
    fun `heart rate service emits availability in given order`() = runBlocking {
        val expectedAvailability1 = DataTypeAvailability.UNKNOWN
        val expectedAvailability2 = DataTypeAvailability.ACQUIRING
        val expectedAvailability3 = DataTypeAvailability.AVAILABLE
        val expectedAvailabilities = listOf(
            expectedAvailability1, expectedAvailability2, expectedAvailability3
        )

        sut = DummyHeartRateService().apply {
            mockAvailabilities = expectedAvailabilities
        }

        val heartRateEmissionCounter = AtomicInteger(0)
        val availabilityCounter = AtomicInteger(0)

        sut?.run {
            retrieveHeartRate().collect {
                when (it) {
                    is MeasureClientData.HeartRateDataPoints -> {
                        availabilityCounter.incrementAndGet()
                    }
                    is MeasureClientData.HeartRateAvailability -> {
                        val currentIndex = availabilityCounter.getAndIncrement()
                        val expectedAvailability = expectedAvailabilities[currentIndex]
                        val actualAvailability = it.availability
                        assertEquals(expectedAvailability, actualAvailability)
                    }
                }
            }
        }
        assertTrue("A heart rate was not emitted.", heartRateEmissionCounter.get() == 0)
        assertTrue("An availability was not emitted.", availabilityCounter.get() == expectedAvailabilities.size)
    }
}