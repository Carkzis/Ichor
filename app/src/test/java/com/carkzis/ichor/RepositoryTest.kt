package com.carkzis.ichor

import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.Value
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Test
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class RepositoryTest {

    var sut: Repository? = null

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `repository emits heart rate data points when received`() = runBlocking {
        val expectedHeartRateDataPoints = listOfHeartRateMeasureData()

        sut = FakeRepository().apply {
            mockHeartRateSample = expectedHeartRateDataPoints
        }

        val heartRateEmissionCounter = AtomicInteger(0)

        sut?.run {
            collectHeartRateFromHeartRateService().collect {
                val actualValue = it.first().value.asDouble()
                val expectedDataPoints =
                    expectedHeartRateDataPoints[heartRateEmissionCounter.get()]
                            as MeasureClientData.HeartRateDataPoints
                val expectedValue = expectedDataPoints.dataPoints.first().value.asDouble()
                assertThat(actualValue, `is`(expectedValue))
                heartRateEmissionCounter.incrementAndGet()
            }
        }

        assertThat(heartRateEmissionCounter.get(), `is`(3))

    }

}