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
import kotlin.math.exp
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class MainViewModelTest {

    var sut: MainViewModel? = null
    var repository: Repository? = null

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `viewmodel retrieves last heart rate data point emitted from repository`() = runTest {
        val listOfDummyHeartRateDataPoints = listOfHeartRateDataPoints()
        val expectedHeartRate = listOfDummyHeartRateDataPoints.last().last()

        repository = FakeRepository().apply {
            mockHeartRateSample = listOfDummyHeartRateDataPoints.map {
                MeasureClientData.HeartRateDataPoints(it)
            }
        }

        sut = MainViewModel(repository as FakeRepository)

        sut?.initiateDataCollection()

        assertThat(sut?.latestHeartRate?.value, `is`(expectedHeartRate.value.asDouble()))
    }

}