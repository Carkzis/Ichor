package com.carkzis.ichor.testdoubles

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataTypeAvailability
import com.carkzis.ichor.data.heartrates.HeartRateService
import com.carkzis.ichor.data.heartrates.MeasureClientData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeHeartRateService : HeartRateService {

    var mockHeartRateSample: List<DataPoint> = listOf()
    var mockAvailability: Availability = DataTypeAvailability.UNKNOWN

    private val measureClientData = MutableSharedFlow<MeasureClientData>()

    suspend fun emitHeartRateDataPoint() = measureClientData.emit(MeasureClientData.HeartRateDataPoints(mockHeartRateSample))
    suspend fun emitAvailability() = measureClientData.emit(MeasureClientData.HeartRateAvailability(mockAvailability))

    override fun retrieveHeartRate(): Flow<MeasureClientData> = measureClientData

}