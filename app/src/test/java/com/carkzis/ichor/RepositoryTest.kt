package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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

// TODO: Need to use a real (but in-memory) database for more representative testing.
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

    @Test
    fun `repository emits data from local database`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateMeasureData()
        val mockDatabase = mutableListOf<LocalHeartRate>()

        sut = FakeRepository(mockDatabase).apply {
            mockHeartRateSample = expectedHeartRateDataPoints
        }

        val heartRateIndex = AtomicInteger(0)

        launch {
            sut?.run {
                collectHeartRatesFromDatabase().take(expectedHeartRateDataPoints.size).collect {
                    for (heartRate in it) {
                        val currentIndex = heartRateIndex.getAndIncrement()
                        assertThat(it[currentIndex].value, `is`(mockDatabase[currentIndex].value.toDouble()))
                    }
                }
            }
        }

        // Artificial delay.
        delay(1)
        assertThat(heartRateIndex.get(), `is`(expectedHeartRateDataPoints.size))
    }

    @Test
    fun `repository emits availability data in order received`() = runTest {
        val expectedAvailabilities = listOfAvailabilities()
        val expectedAvailabilitiesAsMeasureData = listOfAvailabilityMeasureData()

        sut = FakeRepository().apply {
            mockAvailabilities = expectedAvailabilitiesAsMeasureData
        }

        val emittedAvailabilities : MutableList<Availability> = mutableListOf()

        launch {
            sut?.run {
                collectAvailabilityFromHeartRateService().take(expectedAvailabilities.size).collect {
                    emittedAvailabilities.add(it)
                }
            }
        }

        // Artificial delay.
        delay(1)
        assertThat(expectedAvailabilities, `is`(emittedAvailabilities))
    }

    @Test
    fun `repository deletes a selected existing item from database`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateMeasureData()
        val mockDatabase = mutableListOf<LocalHeartRate>()

        sut = FakeRepository(mockDatabase).apply {
            mockHeartRateSample = expectedHeartRateDataPoints
        }

        launch {
            sut?.run {
                collectHeartRatesFromDatabase().take(expectedHeartRateDataPoints.size).collect {}
                assertThat(mockDatabase.size, `is`(3))
                deleteHeartRateFromDatabase(mockDatabase[0].pk)
                assertThat(mockDatabase.size, `is`(2))
            }
        }

        delay(1)
    }

    @Test
    fun `repository does not delete an item from database if primary key does not match any existing items`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateMeasureData()
        val mockDatabase = mutableListOf<LocalHeartRate>()

        sut = FakeRepository(mockDatabase).apply {
            mockHeartRateSample = expectedHeartRateDataPoints
        }

        launch {
            sut?.run {
                collectHeartRatesFromDatabase().take(expectedHeartRateDataPoints.size).collect {}
                assertThat(mockDatabase.size, `is`(3))
                deleteHeartRateFromDatabase("thisPkIsNotInDatabase")
                assertThat(mockDatabase.size, `is`(3))
            }
        }

        delay(1)
    }

}