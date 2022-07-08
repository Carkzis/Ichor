package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : Repository {

    var mockHeartRateSample: List<MeasureClientData> = listOf()
    var mockAvailabilities: List<MeasureClientData> = listOf()

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromDatabase(): Flow<HeartRateDataPoint> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromHeartRateService(): Flow<List<HeartRateDataPoint>> = flow {
        for (measureClientData in mockHeartRateSample) {
            val listOfDataPoints = (measureClientData as MeasureClientData.HeartRateDataPoints).dataPoints
            emit(listOfDataPoints)
        }
    }

}