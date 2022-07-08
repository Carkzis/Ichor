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
        val expectedHeartRate1 = 50.0
        val expectedHeartRate2 = 55.0
        val expectedHeartRate3 = 45.0
        val expectedHeartRateDataPoints = listOf(
            DataPoint.createSample(
                DataType.HEART_RATE_BPM,
                Value.ofDouble(expectedHeartRate1),
                Duration.ofSeconds(0)
            ),
            DataPoint.createSample(
                DataType.HEART_RATE_BPM,
                Value.ofDouble(expectedHeartRate2),
                Duration.ofSeconds(0)
            ),
            DataPoint.createSample(
                DataType.HEART_RATE_BPM,
                Value.ofDouble(expectedHeartRate3),
                Duration.ofSeconds(0)
            ),
        )

        sut = FakeRepository().apply {
            mockHeartRateSample = expectedHeartRateDataPoints.map {
                MeasureClientData.HeartRateDataPoints(listOf(it))
            }
        }

        val heartRateEmissionCounter = AtomicInteger(0)

        sut?.run {
            collectHeartRateFromHeartRateService().collect {
                when (heartRateEmissionCounter.incrementAndGet()) {
                    1 -> assertThat(it.first().value.asDouble(), `is`(expectedHeartRate1))
                    2 -> assertThat(it.first().value.asDouble(), `is`(expectedHeartRate2))
                    3 -> assertThat(it.first().value.asDouble(), `is`(expectedHeartRate3))
                }
            }
        }

        assertThat(heartRateEmissionCounter.get(), `is`(3))

    }

}