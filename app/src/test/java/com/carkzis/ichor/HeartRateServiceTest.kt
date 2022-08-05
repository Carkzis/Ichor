package com.carkzis.ichor

import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.Value
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class HeartRateServiceTest {

    private var sut: HeartRateService? = null

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `heart rate service emits heart rate data points in given order`() = runBlocking {
        val expectedHeartRateDataPoints = listOfHeartRateDataPoints()

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
                        val expectedHeartRateSample = expectedHeartRateDataPoints[currentIndex]
                        val actualHeartRateSample = it.dataPoints
                        assertThat(actualHeartRateSample, `is`(expectedHeartRateSample))
                    }
                    is MeasureClientData.HeartRateAvailability -> {
                        availabilityCounter.incrementAndGet()
                    }
                }
            }
        }

        assertThat(heartRateEmissionCounter.get(), `is`(expectedHeartRateDataPoints.size))
        assertThat(availabilityCounter.get(), `is`(0))

    }

    @Test
    fun `heart rate service emits availability in given order`() = runBlocking {
        val expectedAvailabilities = listOfAvailabilities()

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
                        assertThat(actualAvailability, `is`(expectedAvailability))
                    }
                }
            }
        }

        assertThat(heartRateEmissionCounter.get(), `is`(0))
        assertThat(availabilityCounter.get(), `is`(expectedAvailabilities.size))

    }
}