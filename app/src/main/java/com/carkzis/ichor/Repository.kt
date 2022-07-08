package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.flow.Flow

typealias HeartRateDataPoint = DataPoint

interface Repository {
    fun collectAvailabilityFromHeartRateService(): Flow<Availability>
    fun collectHeartRateFromDatabase() : Flow<HeartRateDataPoint>
    fun collectHeartRateFromHeartRateService() : Flow<List<HeartRateDataPoint>>
}