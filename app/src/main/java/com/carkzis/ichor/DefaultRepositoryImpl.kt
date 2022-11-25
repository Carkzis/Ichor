package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(private val database: IchorDatabase, private val heartRateService: HeartRateService) : Repository {

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> = flow {
        Timber.e("Entered collectAvailabilityFromHeartRateService.")
        heartRateService.retrieveHeartRate().collect {
            when (it) {
                is MeasureClientData.HeartRateDataPoints -> {}
                is MeasureClientData.HeartRateAvailability -> {
                    emit(it.availability)
                }
            }
        }
    }

    override suspend fun collectHeartRatesFromDatabase(): Flow<List<DomainHeartRate>> =
        database.heartRateDao()
            .getAllLocalHeartRates().map { listOfLocalHeartRates ->
                listOfLocalHeartRates.toDomainHeartRate()
            }.flowOn(Dispatchers.IO)

    override suspend fun deleteHeartRateFromDatabase(primaryKey: String) {
        withContext(Dispatchers.IO) {
            database.heartRateDao().deleteLocalHeartRate(primaryKey)
        }
    }

    override suspend fun deleteAllHeartRatesFromDatabase() {
        withContext(Dispatchers.IO) {
            database.heartRateDao().deleteAllLocalHeartRates()
        }
    }

    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<HeartRateDataPoint> = flow {
        Timber.e("Entered collectHeartRateFromHeartRateService.")
        coroutineScope {
            Timber.e("Entered coroutineScope.")
            var shouldSampleDatabase = false
            launch {
                sampler.sampleAtIntervals().collect {
                    shouldSampleDatabase = true
                }
            }
            heartRateService.retrieveHeartRate().collect {
                when (it) {
                    is MeasureClientData.HeartRateDataPoints -> {
                        val latestDatapoint = it.dataPoints.last()
                        emit(latestDatapoint)
                        if (shouldSampleDatabase) {
                            insertValueIntoDatabase(latestDatapoint)
                            shouldSampleDatabase = false
                        }
                    }
                    is MeasureClientData.HeartRateAvailability -> {}
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun insertValueIntoDatabase(heartRate: DataPoint) {
        database.heartRateDao().insertHeartRate(heartRate.toLocalHeartRate())
    }
}
