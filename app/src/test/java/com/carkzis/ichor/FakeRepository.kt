package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FakeRepository(database: MutableList<LocalHeartRate>) : Repository {

    var mockHeartRateSample: List<MeasureClientData> = listOf()
    var mockAvailabilities: List<MeasureClientData> = listOf()
    var mockDatabase = database

    private var shouldSampleDatabase = false

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromDatabase(): Flow<HeartRateDataPoint> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromHeartRateService(): Flow<List<HeartRateDataPoint>> = flow {
        coroutineScope {
            launch {
                initiateHeartRateSampler()
            }
            for (measureClientData in mockHeartRateSample) {
                delay(500)
                val listOfDataPoints = (measureClientData as MeasureClientData.HeartRateDataPoints).dataPoints
                if (shouldSampleDatabase) {
                    insertValueIntoDatabase(listOfDataPoints.last())
                    shouldSampleDatabase = false
                }
                emit(listOfDataPoints)
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun initiateHeartRateSampler() {
        Sampler().sampleAtIntervals(1000, 1000).onEach {
            shouldSampleDatabase = true
        }.collect()
    }

    private fun insertValueIntoDatabase(heartRate: DataPoint) {
        mockDatabase.add(
            LocalHeartRate(
                pk = "1",
                date = "01/01/1900",
                value = heartRate.value.asDouble().toString()
            )
        )
    }
}