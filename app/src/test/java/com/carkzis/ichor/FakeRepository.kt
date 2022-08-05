package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FakeRepository : Repository {

    var mockHeartRateSample: List<MeasureClientData> = listOf()
    var mockAvailabilities: List<MeasureClientData> = listOf()

    var shouldSampleDatabase = false

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromDatabase(): Flow<HeartRateDataPoint> {
        TODO("Not yet implemented")
    }

    override suspend fun collectHeartRateFromHeartRateService(): Flow<List<HeartRateDataPoint>> = flow {
        coroutineScope {
            launch {
                initiateSampler()
            }
            for (measureClientData in mockHeartRateSample) {
                val listOfDataPoints = (measureClientData as MeasureClientData.HeartRateDataPoints).dataPoints
                if (shouldSampleDatabase) {
                    insertValueIntoDatabase(listOfDataPoints.last())
                    shouldSampleDatabase = false
                }
                emit(listOfDataPoints)
            }
        }
    }

    private suspend fun initiateSampler() {
        Sampler().sampleAtIntervals(1000, 1000).onEach {
            shouldSampleDatabase = true
        }.collect()
    }

    private fun insertValueIntoDatabase(listOfDataPoints: DataPoint) {

    }

}