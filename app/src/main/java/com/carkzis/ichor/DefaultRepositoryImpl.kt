package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.room.PrimaryKey
import androidx.test.core.app.ActivityScenario.launch
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
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
