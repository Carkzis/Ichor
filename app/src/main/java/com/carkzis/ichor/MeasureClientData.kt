package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint

sealed class MeasureClientData {
    class HeartRateDataPoints(dataPoints: List<DataPoint>) : MeasureClientData()
    class HeartRateAvailability(availability: Availability) : MeasureClientData()
}