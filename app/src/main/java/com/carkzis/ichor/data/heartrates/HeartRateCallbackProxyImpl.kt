package com.carkzis.ichor.data.heartrates

import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType

class HeartRateCallbackProxyImpl : HeartRateCallbackProxy {
    private var callback: MeasureCallback? = null

    override fun invokeOnAvailabilityChanged(dataType: DataType, availability: Availability) {
        callback?.onAvailabilityChanged(dataType, availability)
    }

    override fun invokeOnData(data: List<DataPoint>) {
        callback?.onData(data)
    }

    override fun retrieveMeasureCallback(heartRateService: HeartRateService): MeasureCallback? {
        return if (heartRateService is HeartRateServiceImpl) {
            callback
        } else {
            null
        }
    }

    override fun addCallback(providedCallback: MeasureCallback) {
        callback = providedCallback
    }
}