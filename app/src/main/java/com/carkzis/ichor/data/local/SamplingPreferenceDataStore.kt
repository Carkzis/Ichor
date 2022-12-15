package com.carkzis.ichor.data.local

import com.carkzis.ichor.utils.SamplingSpeed
import kotlinx.coroutines.flow.Flow

interface SamplingPreferenceDataStore {
    suspend fun collectSamplingPreference() : Flow<SamplingSpeed>
    suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed)
}