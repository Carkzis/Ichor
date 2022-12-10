package com.carkzis.ichor

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
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

}