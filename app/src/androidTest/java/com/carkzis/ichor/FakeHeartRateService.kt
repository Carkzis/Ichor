package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataTypeAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeHeartRateService : HeartRateService {

    var mockHeartRateSample: List<DataPoint> = listOf()
    var mockAvailability: Availability = DataTypeAvailability.UNKNOWN

    private val measureClientData = MutableSharedFlow<MeasureClientData>()

    suspend fun emitHeartRateDataPoint() = measureClientData.emit(MeasureClientData.HeartRateDataPoints(mockHeartRateSample))
    suspend fun emitAvailability() = measureClientData.emit(MeasureClientData.HeartRateAvailability(mockAvailability))

    override fun retrieveHeartRate(): Flow<MeasureClientData> = measureClientData

}