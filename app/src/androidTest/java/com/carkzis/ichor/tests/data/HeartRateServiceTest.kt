package com.carkzis.ichor.tests.data

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.DataType
import androidx.test.platform.app.InstrumentationRegistry
import com.carkzis.ichor.data.HeartRateServiceImpl
import com.carkzis.ichor.data.MeasureClientData
import com.carkzis.ichor.listOfAvailabilities
import com.carkzis.ichor.listOfHeartRateDataPoints
import com.carkzis.ichor.testdoubles.FakeHeartRateCallbackProxy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class HeartRateServiceTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val healthServices = HealthServices.getClient(context)
    private val fakeHeartRateCallbackProxy = FakeHeartRateCallbackProxy()
    private val sut = HeartRateServiceImpl(healthServices, fakeHeartRateCallbackProxy)

    @Test
    fun `heart rate service emits availability in given order`() = runTest {
        val expectedAvailabilities = listOfAvailabilities()

        val availabilityCounter = AtomicInteger(0)

        launch {
            for (dataPointIndex in expectedAvailabilities.indices) {
                delay(100)
                fakeHeartRateCallbackProxy.invokeOnAvailabilityChanged(
                    DataType.HEART_RATE_BPM,
                    expectedAvailabilities[dataPointIndex]
                )
            }
        }

        val availabilityHistory = mutableListOf<MeasureClientData.HeartRateAvailability>()
        sut.retrieveHeartRate().takeWhile {
            availabilityCounter.get() < expectedAvailabilities.size
        }.collect {
            when (it) {
                is MeasureClientData.HeartRateDataPoints -> {}
                is MeasureClientData.HeartRateAvailability -> {
                    availabilityCounter.getAndIncrement()
                    availabilityHistory.add(it)
                }
            }
        }

        for (availabilityIndex in expectedAvailabilities.indices) {
            assertThat(
                availabilityHistory[availabilityIndex].availability,
                `is`(expectedAvailabilities[availabilityIndex])
            )
        }

        assertThat(availabilityCounter.get(), `is`(expectedAvailabilities.size))
    }


    @Test
    fun `heart rate service emits heart rate data points in given order`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateDataPoints()

        val heartRateEmissionCounter = AtomicInteger(0)

        val heartRateDataPointHistory = mutableListOf<MeasureClientData.HeartRateDataPoints>()
        launch {
            for (dataPointIndex in expectedHeartRateDataPoints.indices) {
                delay(100)
                fakeHeartRateCallbackProxy.invokeOnData(expectedHeartRateDataPoints[dataPointIndex])
            }
        }

        sut.retrieveHeartRate().takeWhile {
            heartRateEmissionCounter.get() < expectedHeartRateDataPoints.size
        }.collect {
            when (it) {
                is MeasureClientData.HeartRateDataPoints -> {
                    heartRateEmissionCounter.getAndIncrement()
                    heartRateDataPointHistory.add(it)
                }
                is MeasureClientData.HeartRateAvailability -> {}
            }
        }

        for (dataPointIndex in expectedHeartRateDataPoints.indices) {
            assertThat(
                heartRateDataPointHistory[dataPointIndex].dataPoints,
                `is`(expectedHeartRateDataPoints[dataPointIndex])
            )
        }

        assertThat(heartRateEmissionCounter.get(), `is`(expectedHeartRateDataPoints.size))
    }


}
