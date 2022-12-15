package com.carkzis.ichor.data

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import com.carkzis.ichor.utils.DefaultSampler
import com.carkzis.ichor.data.DomainHeartRate
import com.carkzis.ichor.ui.SamplingSpeed
import com.carkzis.ichor.utils.Sampler
import kotlinx.coroutines.flow.Flow

typealias HeartRateDataPoint = DataPoint

interface Repository {
    suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability>
    suspend fun collectHeartRatesFromDatabase() : Flow<List<DomainHeartRate>>
    suspend fun deleteHeartRateFromDatabase(primaryKey: String)
    suspend fun deleteAllHeartRatesFromDatabase()
    suspend fun collectHeartRateFromHeartRateService(sampler: Sampler = DefaultSampler()) : Flow<HeartRateDataPoint>
    suspend fun collectSamplingPreference() : Flow<SamplingSpeed>
    suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed)
    suspend fun startSharedFlowForDataCollectionFromHeartRateService()
}