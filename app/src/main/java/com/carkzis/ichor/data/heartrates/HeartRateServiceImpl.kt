package com.carkzis.ichor.data.heartrates

import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class HeartRateServiceImpl @Inject constructor(
    healthServicesClient: HealthServicesClient,
    private val heartRateCallbackDelegate: MeasureCallbackDelegate
) : HeartRateService {

    private val heartRateMeasureClient = healthServicesClient.measureClient

    override fun retrieveHeartRate(): Flow<MeasureClientData> = callbackFlow {
        Timber.e("Entered retrieveHeartRate.")
        val heartRateCallback = object : MeasureCallback {
            override fun onAvailabilityChanged(dataType: DataType, availability: Availability) {
                trySendBlocking(MeasureClientData.HeartRateAvailability(availability))
            }

            override fun onData(data: List<DataPoint>) {
                Timber.e("Attempting to send heart rate data. $data")
                trySendBlocking(MeasureClientData.HeartRateDataPoints(data))
            }
        }
        Timber.e("Should not change: $heartRateCallback")
        heartRateCallbackDelegate.addCallback(heartRateCallback)

        heartRateMeasureClient.registerCallback(
            DataType.HEART_RATE_BPM,
            heartRateCallbackDelegate.retrieveMeasureCallback(this@HeartRateServiceImpl) as MeasureCallback
        )

        awaitClose {
            heartRateMeasureClient.unregisterCallback(
                DataType.HEART_RATE_BPM,
                heartRateCallbackDelegate.retrieveMeasureCallback(this@HeartRateServiceImpl) as MeasureCallback
            )
        }
    }
}



