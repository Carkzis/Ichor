package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataTypeAvailability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DummyHeartRateService : HeartRateService {

    var mockHeartRateSample: List<DataPoint> = listOf()
    var mockAvailabilities: List<Availability> = listOf()

    override fun retrieveHeartRate(): Flow<MeasureClientData> = flow {
        for (dataPoint in mockHeartRateSample) {
            emit(MeasureClientData.HeartRateDataPoints(listOf(dataPoint)))
        }

        for (availability in mockAvailabilities) {
            emit(MeasureClientData.HeartRateAvailability(availability))
        }
    }

}