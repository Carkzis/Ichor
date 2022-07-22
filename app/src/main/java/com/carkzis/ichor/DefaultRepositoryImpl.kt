package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(private val heartRateService: HeartRateService) : Repository {

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromDatabase(): Flow<HeartRateDataPoint> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromHeartRateService(): Flow<List<HeartRateDataPoint>> = flow {
        Timber.e("Entered collectHeartRateFromHeartRateService.")
        heartRateService.retrieveHeartRate().collect {
            when (it) {
                is MeasureClientData.HeartRateDataPoints -> {
                    emit(it.dataPoints)
                }
                is MeasureClientData.HeartRateAvailability -> {}
            }
        }
    }

}