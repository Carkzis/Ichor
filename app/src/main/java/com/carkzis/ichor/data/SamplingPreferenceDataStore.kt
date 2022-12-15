package com.carkzis.ichor.data

import com.carkzis.ichor.ui.SamplingSpeed
import kotlinx.coroutines.flow.Flow

interface SamplingPreferenceDataStore {
    suspend fun collectSamplingPreference() : Flow<SamplingSpeed>
    suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed)
}