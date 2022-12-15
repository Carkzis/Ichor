package com.carkzis.ichor.data.heartrates

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint

sealed class MeasureClientData {
    class HeartRateDataPoints(val dataPoints: List<DataPoint>) : MeasureClientData()
    class HeartRateAvailability(val availability: Availability) : MeasureClientData()
}