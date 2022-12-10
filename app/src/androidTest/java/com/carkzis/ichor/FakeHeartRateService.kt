package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeHeartRateService : HeartRateService {

    var mockHeartRateSample: List<List<DataPoint>> = listOf()
    var mockAvailabilities: List<Availability> = listOf()

    override fun retrieveHeartRate(): Flow<MeasureClientData> = flow {
        for (dataPointSample in mockHeartRateSample) {
            emit(MeasureClientData.HeartRateDataPoints(dataPointSample))
        }

        for (availability in mockAvailabilities) {
            emit(MeasureClientData.HeartRateAvailability(availability))
        }
    }

}