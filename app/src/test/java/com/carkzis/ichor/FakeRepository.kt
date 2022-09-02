package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    // TODO: 1. Make sampler use constructor arguments instead of function arguments.
    // TODO: 2. Change test to use in-memory database.
    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<List<HeartRateDataPoint>> =
        flow {
            coroutineScope {
                var shouldSampleDatabase = true
                launch {
                    initiateHeartRateSampler(sampler).collect {
                        shouldSampleDatabase = it
                    }
                }
                for (measureClientData in mockHeartRateSample) {
                    delay(sampleRateFromHeart)
                    val listOfDataPoints =
                        (measureClientData as MeasureClientData.HeartRateDataPoints).dataPoints
                    if (shouldSampleDatabase) {
                        insertValueIntoDatabase(listOfDataPoints.last())
                        shouldSampleDatabase = false
                    }
                    emit(listOfDataPoints)
                }
            }
        }

    private fun initiateHeartRateSampler(sampler: Sampler) = flow {
        sampler.sampleAtIntervals().onEach {
            emit(true)
        }
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