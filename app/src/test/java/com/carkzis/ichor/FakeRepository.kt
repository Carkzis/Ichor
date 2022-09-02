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

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromDatabase(): Flow<HeartRateDataPoint> {
        TODO("Not yet implemented")
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

    private fun insertValueIntoDatabase(heartRate: DataPoint) {
        mockDatabase.add(heartRate.toLocalHeartRate())
    }
}