package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import kotlinx.coroutines.flow.Flow

class FakeRepository : Repository {

    var mockHeartRateSample: List<MeasureClientData> = listOf()
    var mockAvailabilities: List<MeasureClientData> = listOf()

    override fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override fun collectHeartRateFromDatabase(): Flow<HeartRateDataPoint> {
        TODO("Not yet implemented")
    }

    override fun collectHeartRateFromHeartRateService(): Flow<List<HeartRateDataPoint>> {
        TODO("Not yet implemented")
    }

}