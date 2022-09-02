package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(private val database: IchorDatabase, private val heartRateService: HeartRateService) : Repository {

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRatesFromDatabase(): Flow<List<DomainHeartRate>> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<HeartRateDataPoint> = flow {
        Timber.e("Entered collectHeartRateFromHeartRateService.")
        heartRateService.retrieveHeartRate().collect {
            when (it) {
                is MeasureClientData.HeartRateDataPoints -> {
                    val latestDatapoint = it.dataPoints.last()
                    emit(latestDatapoint)
                }
                is MeasureClientData.HeartRateAvailability -> {}
            }
        }
    }
}

//fun HeartRateDataPoint.to