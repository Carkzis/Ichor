package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.flow.Flow

typealias HeartRateDataPoint = DataPoint

interface Repository {
    suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability>
    suspend fun collectHeartRateFromDatabase() : Flow<HeartRateDataPoint>
    suspend fun collectHeartRateFromHeartRateService(sampler: Sampler = Sampler()) : Flow<List<HeartRateDataPoint>>
}