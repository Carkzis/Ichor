package com.carkzis.ichor

import kotlinx.coroutines.flow.Flow

interface HeartRateService {
    fun retrieveHeartRate(): Flow<MeasureClientData>
}