package com.carkzis.ichor.data

import kotlinx.coroutines.flow.Flow

interface HeartRateService {
    fun retrieveHeartRate(): Flow<MeasureClientData>
}