package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
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
}