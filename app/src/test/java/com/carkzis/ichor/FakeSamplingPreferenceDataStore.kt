package com.carkzis.ichor

import kotlinx.coroutines.flow.Flow

class FakeSamplingPreferenceDataStore : SamplingPreferenceDataStore {
    override suspend fun collectSamplingPreference(): Flow<SamplingSpeed> {
        TODO("Not yet implemented")
    }

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {
        TODO("Not yet implemented")
    }
}