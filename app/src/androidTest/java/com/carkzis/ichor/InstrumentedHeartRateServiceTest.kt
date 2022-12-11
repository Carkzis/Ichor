package com.carkzis.ichor

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.test.platform.app.InstrumentationRegistry
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
    fun `heart rate service emits heart rate data points in given order`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateDataPoints()

        val heartRateEmissionCounter = AtomicInteger(0)
        val availabilityCounter = AtomicInteger(0)

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
                is MeasureClientData.HeartRateAvailability -> {
                    availabilityCounter.incrementAndGet()
                }
            }

        }

        for (dataPointIndex in expectedHeartRateDataPoints.indices) {
            assertThat(
                heartRateDataPointHistory[dataPointIndex].dataPoints,
                `is`(expectedHeartRateDataPoints[dataPointIndex])
            )
        }

        assertThat(heartRateEmissionCounter.get(), `is`(expectedHeartRateDataPoints.size))
        assertThat(availabilityCounter.get(), `is`(0))
    }

}
