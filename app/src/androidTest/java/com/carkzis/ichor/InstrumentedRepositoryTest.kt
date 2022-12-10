package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class InstrumentedRepositoryTest {

    private lateinit var database: IchorDatabase
    private lateinit var samplingPreferenceDataStore: FakeSamplingPreferenceDataStore
    private lateinit var heartRateService: FakeHeartRateService
    private lateinit var sut: Repository

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
        val expectedHeartRateDataPoints = listOfHeartRateDataPoints()

        val heartRateHistory = mutableListOf<HeartRateDataPoint>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            sut.collectHeartRateFromHeartRateService(sampler = CustomSampler(intervalInMs = 0)).toList(heartRateHistory)
        }

        for (heartRateDataPoint in expectedHeartRateDataPoints) {
            heartRateService.mockHeartRateSample = heartRateDataPoint
            heartRateService.emitHeartRateDataPoint()
        }

        runCurrent()
        advanceTimeBy(1)

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

        runCurrent()
        advanceTimeBy(1)

        collectJob.cancel()

        assertThat(availabilityHistory.size, `is`(expectedAvailabilities.size))
        assertThat(availabilityHistory, `is`(expectedAvailabilities))
    }

}