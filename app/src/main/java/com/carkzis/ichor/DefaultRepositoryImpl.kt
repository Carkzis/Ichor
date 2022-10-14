package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.test.core.app.ActivityScenario.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(private val database: IchorDatabase, private val heartRateService: HeartRateService) : Repository {

    private var shouldSampleDatabase = AtomicBoolean(false)

    suspend fun startSampling(sampler: Sampler) {
        coroutineScope {
            Timber.e("Entered coroutineScope of sampling.")
            launch {
                sampler.sampleAtIntervals().collect {
                    Timber.e("SHOULD WE SAMPLE $shouldSampleDatabase")
                    shouldSampleDatabase.getAndSet(true)
                    Timber.e("WE ARE OKAY TO SAMPLE $shouldSampleDatabase")
                }
            }
        }
    }

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

    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<HeartRateDataPoint> = flow {
        Timber.e("Entered collectHeartRateFromHeartRateService.")
        coroutineScope {
            Timber.e("Entered coroutineScope.")
//            var shouldSampleDatabase = false
//            launch {
//                sampler.sampleAtIntervals().collect {
//                    Timber.e("SHOULD WE SAMPLE $shouldSampleDatabase")
//                    shouldSampleDatabase = true
//                    Timber.e("WE ARE OKAY TO SAMPLE $shouldSampleDatabase")
//                }
//            }
            heartRateService.retrieveHeartRate().collect {
                when (it) {
                    is MeasureClientData.HeartRateDataPoints -> {
                        val latestDatapoint = it.dataPoints.last()
                        emit(latestDatapoint)
                        Timber.e("SHOULD WE INSERT INTO DATABASE $shouldSampleDatabase")
                        if (shouldSampleDatabase.get()) {
                            Timber.e("WE WILL INSERT INTO DATABASE $shouldSampleDatabase")
                            insertValueIntoDatabase(latestDatapoint)
                            shouldSampleDatabase.getAndSet(false)
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
