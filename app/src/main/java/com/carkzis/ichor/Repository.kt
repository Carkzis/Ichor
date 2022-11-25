package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.flow.Flow

typealias HeartRateDataPoint = DataPoint

interface Repository {
    suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability>
    suspend fun collectHeartRatesFromDatabase() : Flow<List<DomainHeartRate>>
    suspend fun deleteHeartRateFromDatabase(primaryKey: String)
    suspend fun collectHeartRateFromHeartRateService(sampler: Sampler = Sampler()) : Flow<HeartRateDataPoint>
}