package com.carkzis.ichor

import kotlinx.coroutines.flow.Flow

interface SamplingPreferenceDataStore {
    suspend fun collectSamplingPreference() : Flow<SamplingSpeed>
    suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed)
}