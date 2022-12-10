package com.carkzis.ichor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeSamplingPreferenceDataStore : SamplingPreferenceDataStore {
    private val samplingSpeedFlow = MutableSharedFlow<SamplingSpeed>()
    private var currentSamplingSpeedPreference = SamplingSpeed.DEFAULT

    suspend fun emit() = samplingSpeedFlow.emit(currentSamplingSpeedPreference)

    override suspend fun collectSamplingPreference(): Flow<SamplingSpeed> = samplingSpeedFlow

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {
        currentSamplingSpeedPreference = samplingSpeed
    }
}