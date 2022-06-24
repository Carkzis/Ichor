package com.carkzis.ichor

import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class HeartRateServiceImpl @Inject constructor(healthServicesClient: HealthServicesClient) : HeartRateService {

    private val heartRateMeasureClient = healthServicesClient.measureClient

    override fun retrieveHeartRate(): Flow<MeasureClientData> = callbackFlow {
        val heartRateCallback = object : MeasureCallback {
            override fun onAvailabilityChanged(dataType: DataType, availability: Availability) {
                trySendBlocking(MeasureClientData.HeartRateAvailability(availability))
            }
            override fun onData(data: List<DataPoint>) {
                trySendBlocking(MeasureClientData.HeartRateDataPoints(data))
            }
        }

        heartRateMeasureClient.registerCallback(DataType.HEART_RATE_BPM, heartRateCallback)

        awaitClose {
            heartRateMeasureClient.unregisterCallback(DataType.HEART_RATE_BPM, heartRateCallback)
        }
    }

}