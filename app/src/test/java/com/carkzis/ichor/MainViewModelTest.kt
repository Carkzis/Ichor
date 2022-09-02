package com.carkzis.ichor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Rule
import org.junit.Test
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

        // Fake delay of 1ms.
        delay(1)

        assertThat(sut?.latestHeartRate?.value, `is`(expectedHeartRate.value.asDouble()))
    }

}