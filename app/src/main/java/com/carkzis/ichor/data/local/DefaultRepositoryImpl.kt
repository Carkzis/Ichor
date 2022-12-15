package com.carkzis.ichor.data.local

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import com.carkzis.ichor.*
import com.carkzis.ichor.data.*
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.data.domain.toDomainHeartRate
import com.carkzis.ichor.data.heartrates.HeartRateService
import com.carkzis.ichor.data.heartrates.MeasureClientData
import com.carkzis.ichor.utils.SamplingSpeed
import com.carkzis.ichor.utils.Sampler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(private val database: IchorDatabase, private val heartRateService: HeartRateService, private val dataStore: SamplingPreferenceDataStore) :
    Repository {

    private var sharedFlow = MutableSharedFlow<MeasureClientData>()
    override suspend fun startSharedFlowForDataCollectionFromHeartRateService() {
        heartRateService.retrieveHeartRate().collect {
            sharedFlow.emit(it)
        }
    }

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> = flow {
        Timber.e("Entered collectAvailabilityFromHeartRateService.")
        //heartRateService.retrieveHeartRate().collect {
        sharedFlow.collect {
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
            sharedFlow.collect {
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

    override suspend fun collectSamplingPreference(): Flow<SamplingSpeed> = dataStore.collectSamplingPreference()

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {
        dataStore.changeSamplingPreference(samplingSpeed)
    }

    private fun insertValueIntoDatabase(heartRate: DataPoint) {
        database.heartRateDao().insertHeartRate(heartRate.toLocalHeartRate())
    }
}
