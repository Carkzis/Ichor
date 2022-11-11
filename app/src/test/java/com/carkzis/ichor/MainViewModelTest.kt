package com.carkzis.ichor

import androidx.health.services.client.data.DataTypeAvailability
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import timber.log.Timber
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
    fun `viewmodel retrieves latest heart rate data point emitted from repository`() = runTest {
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

    @Test
    fun `viewmodel retrieves all heart rates emitted from repository`() = runTest {
        val mockDatabase = listOfHeartRateDataAsMockDatabase()
        val expectedDomainHeartRates = mockDatabase.toDomainHeartRate()
        val repository = FakeRepository(mockDatabase as MutableList)

        sut = MainViewModel(repository)

        sut?.initiateDataCollection()

        // Fake delay of 1ms.
        delay(1)

        assertThat(sut?.latestHeartRateList?.value, `is`(expectedDomainHeartRates))
    }

    @Test
    fun `viewmodel retrieves latest availability emitted from repository`() = runTest {
        val expectedAvailability = DataTypeAvailability.AVAILABLE
        val repository = FakeRepository().apply {
            mockAvailabilities = listOfAvailabilityMeasureData()
        }

        sut = MainViewModel(repository)
        sut?.initiateDataCollection()

        // Fake delay of 1ms.
        delay(1)

        assertThat(sut?.latestAvailability?.value, `is`(expectedAvailability))
    }

    @Test
    fun `viewmodel deletes heart rate from database via repository`() = runTest {
        val mockDatabase = listOfHeartRateDataAsMockDatabase()
        val repository = FakeRepository(mockDatabase as MutableList)
        val heartRateDataToDelete = mockDatabase[1]

        sut = MainViewModel(repository)

        sut?.initiateDataCollection()

        sut?.deleteHeartRate(heartRateDataToDelete.pk)

        // Fake delay of 1ms.
        delay(1)

        val attemptFilterForDeletedHeartRate = mockDatabase.filter {
            it.pk == heartRateDataToDelete.pk
        }
        assertThat(attemptFilterForDeletedHeartRate.size, `is`(0))
    }

    @Test
    fun `viewmodel does not deletes heart rate from database via repository`() = runTest {
        val mockDatabase = listOfHeartRateDataAsMockDatabase()
        val repository = FakeRepository(mockDatabase as MutableList)
        val heartRateDataToDelete = mockDatabase[1]

        sut = MainViewModel(repository)

        sut?.initiateDataCollection()

        sut?.deleteHeartRate("thisPkIsNotInDatabase")

        // Fake delay of 1ms.
        delay(1)

        val attemptFilterForDeletedHeartRate = mockDatabase.filter {
            it.pk == heartRateDataToDelete.pk
        }
        assertThat(attemptFilterForDeletedHeartRate.size, `is`(1))
    }

}