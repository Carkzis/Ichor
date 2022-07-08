package com.carkzis.ichor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.Value
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class MainViewModelTest {

    var sut: MainViewModel? = null
    var repository: Repository? = null

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `viewmodel retrieves last heart rate data point emitted from repository`() = runTest {
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

        repository = FakeRepository().apply {
            mockHeartRateSample = expectedHeartRateDataPoints.map {
                MeasureClientData.HeartRateDataPoints(listOf(it))
            }
        }

        sut = MainViewModel(repository as FakeRepository)

        sut?.assignLatestHeartRateToUI()

        assertThat(sut?.latestHeartRate?.value, `is`(45.0))
    }
}