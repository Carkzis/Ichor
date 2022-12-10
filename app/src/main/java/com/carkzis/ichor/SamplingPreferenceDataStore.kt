package com.carkzis.ichor

import kotlinx.coroutines.flow.Flow

interface SamplingPreferenceDataStore {
    suspend fun collectSamplingPreference() : Flow<String>
    suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed)
}