package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint

sealed class MeasureClientData {
    class HeartRateDataPoints(val dataPoints: List<DataPoint>) : MeasureClientData()
    class HeartRateAvailability(val availability: Availability) : MeasureClientData()
}