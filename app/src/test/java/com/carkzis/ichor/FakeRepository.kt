package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FakeRepository(database: MutableList<LocalHeartRate> = mutableListOf()) : Repository {

    var mockHeartRateSample: List<MeasureClientData> = listOf()
    var mockAvailabilities: List<MeasureClientData> = listOf()
    var mockDatabase = database
    var sampleRateFromHeart = 0L
    var dataStore = SamplingSpeed.DEFAULT

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> = flow {
        for (availabilityData in mockAvailabilities) {
            val heartRateAvailability = availabilityData as MeasureClientData.HeartRateAvailability
            val domainAvailability = heartRateAvailability.availability
            emit(domainAvailability)
        }
    }

    override suspend fun collectHeartRatesFromDatabase(): Flow<List<DomainHeartRate>> = flow {
        for (measureClientData in mockHeartRateSample) {
            val listOfDataPoints =
                (measureClientData as MeasureClientData.HeartRateDataPoints).dataPoints
            val latestDataPoint = listOfDataPoints.last()
            insertValueIntoDatabase(latestDataPoint)
        }
        val domainHeartRates = mockDatabase.toDomainHeartRate()
        emit(domainHeartRates)
    }

    override suspend fun deleteHeartRateFromDatabase(primaryKey: String) {
        mockDatabase.removeIf {
            it.pk == primaryKey
        }
    }

    override suspend fun deleteAllHeartRatesFromDatabase() {
        mockDatabase.clear()
    }

    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<HeartRateDataPoint> =
        flow {
            coroutineScope {
                var shouldSampleDatabase = false
                launch {
                    sampler.sampleAtIntervals().collect {
                        shouldSampleDatabase = true
                    }
                }
                for (measureClientData in mockHeartRateSample) {
                    delay(sampleRateFromHeart)
                    val listOfDataPoints =
                        (measureClientData as MeasureClientData.HeartRateDataPoints).dataPoints
                    val latestDataPoint = listOfDataPoints.last()
                    if (shouldSampleDatabase) {
                        insertValueIntoDatabase(latestDataPoint)
                        shouldSampleDatabase = false
                    }
                    emit(latestDataPoint)
                }
                this.cancel()
            }
        }

    override suspend fun collectSamplingPreference(): Flow<SamplingSpeed> = flow {
        emit(dataStore)
    }

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {
        dataStore = samplingSpeed
    }

    private fun insertValueIntoDatabase(heartRate: DataPoint) {
        mockDatabase.add(heartRate.toLocalHeartRate())
    }
}