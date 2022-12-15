package com.carkzis.ichor.data.heartrates

import kotlinx.coroutines.flow.Flow

interface HeartRateService {
    fun retrieveHeartRate(): Flow<MeasureClientData>
}