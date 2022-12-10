package com.carkzis.ichor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SamplingPreferenceDataStoreTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var samplingPreferenceDataStore: SamplingPreferenceDataStore
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("test_datastore")}
        )
        samplingPreferenceDataStore = SamplingPreferenceDataStoreImpl(dataStore)
    }

    @After
    fun tearDown() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }

    // TODO: Tests for SamplingPreferenceDataStore, yet to be created but will be a wrapper.
    @Test
    fun `can collect default preference from data store`() = runTest {
        val samplingPreference = samplingPreferenceDataStore.collectSamplingPreference().take(1).toList()[0]
        assertThat(samplingPreference, `is`(SamplingSpeed.DEFAULT.toString()))
    }

}