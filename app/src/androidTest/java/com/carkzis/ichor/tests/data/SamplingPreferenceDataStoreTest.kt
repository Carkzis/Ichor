package com.carkzis.ichor.tests.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.carkzis.ichor.data.local.SamplingPreferenceDataStore
import com.carkzis.ichor.data.local.SamplingPreferenceDataStoreImpl
import com.carkzis.ichor.utils.SamplingSpeed
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SamplingPreferenceDataStoreTest {

    private lateinit var preferencesScope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var samplingPreferenceDataStore: SamplingPreferenceDataStore
    private val testDataStoreFileName = "test_datastore"
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        preferencesScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        dataStore = PreferenceDataStoreFactory.create(
            scope = preferencesScope,
            produceFile = { context.preferencesDataStoreFile(testDataStoreFileName)}
        )
        samplingPreferenceDataStore = SamplingPreferenceDataStoreImpl(dataStore)
    }

    @After
    fun tearDown() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
            File(context.filesDir, testDataStoreFileName).deleteRecursively()
            preferencesScope.cancel()
        }
    }

    @Test
    fun `can collect default preference from data store`() = runTest {
        val expectedDefaultSamplingPreference = SamplingSpeed.DEFAULT
        val actualDefaultSamplingPreference = samplingPreferenceDataStore.collectSamplingPreference().take(1).toList()[0]
        assertThat(actualDefaultSamplingPreference, `is`(expectedDefaultSamplingPreference))
    }

    @Test
    fun `can collect new preference from data store when changed`() = runTest {
        val expectedNewSamplingSpeed = SamplingSpeed.FAST

        val samplingSpeedPreferenceHistory = mutableListOf<SamplingSpeed>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            samplingPreferenceDataStore.collectSamplingPreference().toList(samplingSpeedPreferenceHistory)
        }

        // Fake delay of 1ms.
        delay(1)
        samplingPreferenceDataStore.changeSamplingPreference(expectedNewSamplingSpeed)

        collectJob.cancel()

        val actualNewSamplingPreference = samplingSpeedPreferenceHistory.last()
        assertThat(actualNewSamplingPreference, `is`(expectedNewSamplingSpeed))
        assertThat(samplingSpeedPreferenceHistory.size, `is`(2))
    }

}