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
import timber.log.Timber
import javax.inject.Inject

open class HeartRateServiceImpl @Inject constructor(
    healthServicesClient: HealthServicesClient,
    private var heartRateCallbackProxy: HeartRateCallbackProxy? = null
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
        heartRateCallbackProxy =
            HeartRateCallbackImpl(callback = heartRateCallback, this@HeartRateServiceImpl)

        heartRateMeasureClient.registerCallback(
            DataType.HEART_RATE_BPM,
            heartRateCallbackProxy?.retrieveMeasureCallback(this@HeartRateServiceImpl) as MeasureCallback
        )

        awaitClose {
            heartRateMeasureClient.unregisterCallback(
                DataType.HEART_RATE_BPM,
                heartRateCallbackProxy?.retrieveMeasureCallback(this@HeartRateServiceImpl) as MeasureCallback
            )
        }
    }
}

interface HeartRateCallbackProxy {
    fun invokeOnAvailabilityChanged(dataType: DataType, availability: Availability)
    fun invokeOnData(data: List<DataPoint>)
    fun retrieveMeasureCallback(heartRateService: HeartRateService): MeasureCallback?
}

class HeartRateCallbackImpl(
    private val callback: MeasureCallback,
    private val heartRateServiceForCallback: HeartRateServiceImpl
) : HeartRateCallbackProxy {
    override fun invokeOnAvailabilityChanged(dataType: DataType, availability: Availability) {
        callback.onAvailabilityChanged(dataType, availability)
    }

    override fun invokeOnData(data: List<DataPoint>) {
        callback.onData(data)
    }

    override fun retrieveMeasureCallback(heartRateService: HeartRateService): MeasureCallback? {
        return if (heartRateService == heartRateServiceForCallback) {
            callback
        } else {
            null
        }
    }

}
