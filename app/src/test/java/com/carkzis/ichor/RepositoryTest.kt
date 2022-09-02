package com.carkzis.ichor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class RepositoryTest {

    var sut: Repository? = null

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `repository emits heart rate data points when received`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateMeasureData()

        sut = FakeRepository().apply {
            mockHeartRateSample = expectedHeartRateDataPoints
        }

        val heartRateEmissionCounter = AtomicInteger(0)

        sut?.run {
            collectHeartRateFromHeartRateService().take(3).collect {
                val actualValue = it.value.asDouble()
                val expectedDataPoints =
                    expectedHeartRateDataPoints[heartRateEmissionCounter.get()]
                            as MeasureClientData.HeartRateDataPoints
                val expectedValue = expectedDataPoints.dataPoints.last().value.asDouble()
                assertThat(actualValue, `is`(expectedValue))
                heartRateEmissionCounter.incrementAndGet()
            }
        }

        assertThat(heartRateEmissionCounter.get(), `is`(3))
    }

    @Test
    fun `repository inserts latest heart rate data into database at given interval`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateMeasureData()
        val mockDatabase = mutableListOf<LocalHeartRate>()

        sut = FakeRepository(mockDatabase).apply {
            mockHeartRateSample = expectedHeartRateDataPoints
            sampleRateFromHeart = 500L
        }

        val heartRateEmissionCounter = AtomicInteger(0)
        val sampleRateForDatabaseInsertion = 1000L
        val initialSampleTimeForDatabaseInsertion = 1000L
        val sampler = Sampler(sampleRateForDatabaseInsertion, initialSampleTimeForDatabaseInsertion)

        launch {
            sut?.run {
                collectHeartRateFromHeartRateService(sampler).take(3).collect {
                    heartRateEmissionCounter.incrementAndGet()
                }
            }
        }

        runCurrent()
        advanceTimeBy(1501)

        assertThat(mockDatabase.size, `is`(1))
        assertThat(heartRateEmissionCounter.get(), `is`(3))
    }

}