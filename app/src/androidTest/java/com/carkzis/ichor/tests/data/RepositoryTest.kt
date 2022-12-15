package com.carkzis.ichor.tests.data

import androidx.health.services.client.data.Availability
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carkzis.ichor.data.DefaultRepositoryImpl
import com.carkzis.ichor.data.DomainHeartRate
import com.carkzis.ichor.data.HeartRateDataPoint
import com.carkzis.ichor.data.IchorDatabase
import com.carkzis.ichor.listOfAvailabilities
import com.carkzis.ichor.listOfHeartRateDataAsMockDatabase
import com.carkzis.ichor.listOfHeartRateDataPoints
import com.carkzis.ichor.testdoubles.FakeHeartRateService
import com.carkzis.ichor.testdoubles.FakeSamplingPreferenceDataStore
import com.carkzis.ichor.ui.SamplingSpeed
import com.carkzis.ichor.utils.CustomSampler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

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
            launch {
                sut.startSharedFlowForDataCollectionFromHeartRateService()
            }
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
            launch {
                sut.startSharedFlowForDataCollectionFromHeartRateService()
            }
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

        // 1. Sample rate from heart is 100ms. There are a total of 3 samples, coming to 300ms.
        val sampleRateFromHeart = 100L
        val samples = 3
        // 2. At initial time to insert into database, 150ms, we will do one database insertion.
        val initialSampleTimeForDatabaseInsertion = 150L
        // 3. No sample at next sample time, 100ms+150ms=450ms, as all samples completed at 300ms.
        val sampleRateForDatabaseInsertion = 200L

        val sampler = CustomSampler(sampleRateForDatabaseInsertion, initialSampleTimeForDatabaseInsertion)

        val heartRateHistory = mutableListOf<HeartRateDataPoint>()

        val collectJob = launch {
            launch {
                sut.startSharedFlowForDataCollectionFromHeartRateService()
            }
            launch {
                sut.collectHeartRateFromHeartRateService(sampler = sampler).toList(heartRateHistory)
            }
        }

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

        val latestHeartRateList = heartRateHistory.last()
        assertThat(latestHeartRateList.size, `is`(expectedHeartRateDataPoints.size))

        heartRateHistory.forEachIndexed { index, heartRateList ->
            assertThat(heartRateList[index].value, `is`(latestHeartRateList[index].value))
        }

    }

    @Test
    fun `repository deletes a selected existing item from database`() = runTest {
        val heartRateDataPoints = listOfHeartRateDataAsMockDatabase()

        for (heartRateDataPoint in heartRateDataPoints) {
            database.heartRateDao().insertHeartRate(heartRate = heartRateDataPoint)
        }

        val heartRateHistoryBeforeDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryBeforeDeletion)
        val heartRateListBeforeDeletion = heartRateHistoryBeforeDeletion[0]

        assertThat(heartRateListBeforeDeletion.size, `is`(heartRateDataPoints.size))

        val heartRateToDeletePk = heartRateDataPoints[0].pk
        sut.deleteHeartRateFromDatabase(heartRateToDeletePk)

        val heartRateHistoryAfterDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryAfterDeletion)
        val heartRateListAfterDeletion = heartRateHistoryAfterDeletion[0]

        assertThat(heartRateListAfterDeletion.size, `is`(heartRateDataPoints.size - 1))
    }

    @Test
    fun `repository does not delete an item from database if primary key does not match any existing items`() = runTest {
        val heartRateDataPoints = listOfHeartRateDataAsMockDatabase()

        for (heartRateDataPoint in heartRateDataPoints) {
            database.heartRateDao().insertHeartRate(heartRate = heartRateDataPoint)
        }

        val heartRateHistoryBeforeAttemptedDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryBeforeAttemptedDeletion)
        val heartRateListBeforeAttemptedDeletion = heartRateHistoryBeforeAttemptedDeletion[0]

        assertThat(heartRateListBeforeAttemptedDeletion.size, `is`(heartRateDataPoints.size))

        val heartRateToNotDeletePk = "thisIsNotAnExistingPk"
        sut.deleteHeartRateFromDatabase(heartRateToNotDeletePk)

        val heartRateHistoryAfterAttemptedDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryAfterAttemptedDeletion)
        val heartRateListAfterAttemptedDeletion = heartRateHistoryAfterAttemptedDeletion[0]

        assertThat(heartRateListAfterAttemptedDeletion.size, `is`(heartRateDataPoints.size))
    }

    @Test
    fun `repository deletes all items from database`() = runTest {
        val heartRateDataPoints = listOfHeartRateDataAsMockDatabase()

        for (heartRateDataPoint in heartRateDataPoints) {
            database.heartRateDao().insertHeartRate(heartRate = heartRateDataPoint)
        }

        val heartRateHistoryBeforeDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryBeforeDeletion)
        val heartRateListBeforeDeletion = heartRateHistoryBeforeDeletion[0]

        assertThat(heartRateListBeforeDeletion.size, `is`(heartRateDataPoints.size))

        sut.deleteAllHeartRatesFromDatabase()

        val heartRateHistoryAfterDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryAfterDeletion)
        val heartRateListAfterDeletion = heartRateHistoryAfterDeletion[0]

        assertThat(heartRateListAfterDeletion.size, `is`(0))
    }

    @Test
    fun `repository can attempt to delete all items from empty database without error`() = runTest {
        val heartRateHistoryBeforeDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryBeforeDeletion)
        val heartRateListBeforeDeletion = heartRateHistoryBeforeDeletion[0]

        assertThat(heartRateListBeforeDeletion.size, `is`(0))

        sut.deleteAllHeartRatesFromDatabase()

        val heartRateHistoryAfterDeletion = mutableListOf<List<DomainHeartRate>>()
        sut.collectHeartRatesFromDatabase().take(1).toList(heartRateHistoryAfterDeletion)
        val heartRateListAfterDeletion = heartRateHistoryAfterDeletion[0]

        assertThat(heartRateListAfterDeletion.size, `is`(0))
    }

}