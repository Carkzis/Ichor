package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import androidx.test.core.app.ActivityScenario.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(private val database: IchorDatabase, private val heartRateService: HeartRateService) : Repository {

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRatesFromDatabase(): Flow<List<DomainHeartRate>> =
        database.heartRateDao()
            .getAllLocalHeartRates().map { listOfLocalHeartRates ->
                listOfLocalHeartRates.toDomainHeartRate()
            }.flowOn(Dispatchers.IO)

    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<HeartRateDataPoint> = flow<HeartRateDataPoint> {
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
                            println("Is this doing anything?")
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
