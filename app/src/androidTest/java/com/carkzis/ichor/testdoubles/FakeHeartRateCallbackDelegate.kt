package com.carkzis.ichor.testdoubles

import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType
import com.carkzis.ichor.data.heartrates.MeasureCallbackDelegate
import com.carkzis.ichor.data.heartrates.HeartRateService

class FakeHeartRateCallbackDelegate : MeasureCallbackDelegate {
    private var callback: MeasureCallback? = null

    override fun invokeOnAvailabilityChanged(dataType: DataType, availability: Availability) {
        callback?.onAvailabilityChanged(dataType, availability)
    }

    override fun invokeOnData(data: List<DataPoint>) {
        callback?.onData(data)
    }

    override fun retrieveMeasureCallback(heartRateService: HeartRateService): MeasureCallback? {
        return callback
    }

    override fun addCallback(providedCallback: MeasureCallback) {
        callback = providedCallback
    }
}