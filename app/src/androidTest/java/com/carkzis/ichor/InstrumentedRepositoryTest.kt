package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.room.Room
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class InstrumentedRepositoryTest {

    private lateinit var database: IchorDatabase
    private lateinit var samplingPreferenceDataStore: FakeSamplingPreferenceDataStore
    private lateinit var heartRateService: FakeHeartRateService
    private lateinit var sut: DefaultRepositoryImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            IchorDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        samplingPreferenceDataStore = FakeSamplingPreferenceDataStore()
        heartRateService = FakeHeartRateService()
        sut = DefaultRepositoryImpl(database, heartRateService, samplingPreferenceDataStore)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `repository obtains default sampling speed from datastore`() = runTest {
        val expectedDefaultSamplingPreference = SamplingSpeed.DEFAULT

        val samplingSpeedPreferenceHistory = mutableListOf<SamplingSpeed>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.collectSamplingPreference().toList(samplingSpeedPreferenceHistory)
        }

        samplingPreferenceDataStore.emit()

        collectJob.cancel()

        val actualNewSamplingPreference = samplingSpeedPreferenceHistory.last()
        assertThat(actualNewSamplingPreference, `is`(expectedDefaultSamplingPreference))
        assertThat(samplingSpeedPreferenceHistory.size, `is`(1))
    }

    @Test
    fun `repository obtains new sampling speed when provided to datastore`() = runTest {
        val expectedNewSamplingSpeed = SamplingSpeed.FAST

        val samplingSpeedPreferenceHistory = mutableListOf<SamplingSpeed>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.collectSamplingPreference().toList(samplingSpeedPreferenceHistory)
        }

        samplingPreferenceDataStore.emit()

        sut.changeSamplingPreference(expectedNewSamplingSpeed)

        samplingPreferenceDataStore.emit()

        collectJob.cancel()

        val actualNewSamplingPreference = samplingSpeedPreferenceHistory.last()
        assertThat(actualNewSamplingPreference, `is`(expectedNewSamplingSpeed))
        assertThat(samplingSpeedPreferenceHistory.size, `is`(2))
    }

    @Test
    fun `repository emits heart rate data points when received`() = runTest {
        val mockHeartRateDataPoint = listOfHeartRateDataPoints()[0]

        val heartRateHistory = mutableListOf<HeartRateDataPoint>()
        val collectJob = launch {
            sut.collectHeartRateFromHeartRateService(sampler = CustomSampler(intervalInMs = 0, initialIntervalInMs = 0)).take(3).toList(heartRateHistory)
        }

        for (heartRateDataPoint in 1..100) {
            delay(1)
            heartRateService.mockHeartRateSample = mockHeartRateDataPoint
            heartRateService.emitHeartRateDataPoint()
        }

        collectJob.cancel()

        assertThat(heartRateHistory.size, `is`(3))
    }

    @Test
    fun `repository emits availability data in order received`() = runTest {
        val expectedAvailabilities = listOfAvailabilities()

        val availabilityHistory = mutableListOf<Availability>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.collectAvailabilityFromHeartRateService().toList(availabilityHistory)
        }

        for (availability in expectedAvailabilities) {
            heartRateService.mockAvailability = availability
            heartRateService.emitAvailability()
        }

        collectJob.cancel()

        assertThat(availabilityHistory.size, `is`(expectedAvailabilities.size))
        assertThat(availabilityHistory, `is`(expectedAvailabilities))
    }

    @Test
    fun `repository inserts latest heart rate data into database at given interval`() = runBlocking {
        // NOTE: Using runBlocking, so that items are inserted into the database as expected.
        val mockHeartRateDataPoint = listOfHeartRateDataPoints()[0]

        val sampleRateFromHeart = 500L
        val sampleRateForDatabaseInsertion = 950L
        val initialSampleTimeForDatabaseInsertion = 500L
        val sampler = CustomSampler(sampleRateForDatabaseInsertion, initialSampleTimeForDatabaseInsertion)

        val heartRateHistory = mutableListOf<HeartRateDataPoint>()
        val collectJob = launch {
            sut.collectHeartRateFromHeartRateService(sampler = sampler).toList(heartRateHistory)
        }

        val samples = (sampleRateForDatabaseInsertion + initialSampleTimeForDatabaseInsertion) / sampleRateFromHeart
        for (sampleEmission in 1..samples) {
            delay(sampleRateFromHeart)
            heartRateService.mockHeartRateSample = mockHeartRateDataPoint
            heartRateService.emitHeartRateDataPoint()
        }

        collectJob.cancel()

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        assertThat(heartRates.size, `is`(1))
    }

    @Test
    fun `repository emits data from local database`() = runTest {
        val expectedHeartRateDataPoints = listOfHeartRateDataAsMockDatabase()

        for (heartRateDataPoint in expectedHeartRateDataPoints) {
            database.heartRateDao().insertHeartRate(heartRate = heartRateDataPoint)
        }

        val heartRateHistory = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistory)

        delay(1)

        val latestHeartRateList = heartRateHistory.last()
        assertThat(latestHeartRateList.size, `is`(expectedHeartRateDataPoints.size))

        heartRateHistory.forEachIndexed { index, heartRateList ->
            assertThat(heartRateList[index].value, `is`(latestHeartRateList[index].value))
        }

    }

}