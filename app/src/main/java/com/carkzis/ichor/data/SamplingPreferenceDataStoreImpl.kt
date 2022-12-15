package com.carkzis.ichor.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.carkzis.ichor.data.SamplingPreferenceDataStore
import com.carkzis.ichor.ui.SamplingSpeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class SamplingPreferenceDataStoreImpl(private val dataStore: DataStore<Preferences>) :
    SamplingPreferenceDataStore {
    private val preferencesFlow : Flow<SamplingSpeed> = dataStore.data.map {
        val samplingPreference = it[PreferenceKeys.SAMPLING_PREFERENCE]?.let { speedAsString ->
            SamplingSpeed.forDescriptor(
                speedAsString
            )
        } ?: SamplingSpeed.DEFAULT
        samplingPreference
    }

    override suspend fun collectSamplingPreference(): Flow<SamplingSpeed> = preferencesFlow.flowOn(Dispatchers.IO)

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {
        dataStore.edit {
            it[PreferenceKeys.SAMPLING_PREFERENCE] = samplingSpeed.toString()
        }
    }

}

object PreferenceKeys {
    val SAMPLING_PREFERENCE = stringPreferencesKey("sampling_preference")
}