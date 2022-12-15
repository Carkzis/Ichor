package com.carkzis.ichor.data.heartrates

import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.health.services.client.data.DataType

interface HeartRateCallbackProxy {
    fun invokeOnAvailabilityChanged(dataType: DataType, availability: Availability)
    fun invokeOnData(data: List<DataPoint>)
    fun retrieveMeasureCallback(heartRateService: HeartRateService): MeasureCallback?
    fun addCallback(providedCallback: MeasureCallback) {
    }
}