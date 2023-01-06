package com.carkzis.ichor.testdoubles

import androidx.health.services.client.data.*
import com.carkzis.ichor.data.*
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.data.domain.toDomainHeartRate
import com.carkzis.ichor.data.local.HeartRateDataPoint
import com.carkzis.ichor.data.local.LocalHeartRate
import com.carkzis.ichor.data.local.Repository
import com.carkzis.ichor.utils.SamplingSpeed
import com.carkzis.ichor.utils.Sampler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.util.*

class DummyRepository : Repository {
    private var dummySamplingSpeed = MutableStateFlow(SamplingSpeed.DEFAULT)
    private val dummyDatabase = MutableStateFlow(mutableListOf(
            LocalHeartRate(
                date = "2022-12-25T12:30:30.303",
                pk = "1",
                value = "100.0"
            ),
            LocalHeartRate(
                date = "2023-12-25T12:30:30.303",
                pk = "2",
                value = "200.0"
            )
        ).toDomainHeartRate())

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> = flow {
        emit(DataTypeAvailability.UNKNOWN)
    }

    override suspend fun collectHeartRatesFromDatabase(): Flow<List<DomainHeartRate>> =
        dummyDatabase

    override suspend fun deleteHeartRateFromDatabase(primaryKey: String) {
        dummyDatabase.value = dummyDatabase.value.filter { it.pk != primaryKey }
    }

    override suspend fun deleteAllHeartRatesFromDatabase() {
        dummyDatabase.value = mutableListOf()
    }

    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<HeartRateDataPoint> =
        flow {
            emit(DataPoint.createSample(
                DataType.HEART_RATE_BPM,
                Value.ofDouble(0.0),
                Duration.ofSeconds(0)
            ))
            // Artificial delay for testing new heartrate displayed.
            delay(500)
            emit(DataPoint.createSample(
                DataType.HEART_RATE_BPM,
                Value.ofDouble(1.0),
                Duration.ofSeconds(0)
            ))
        }

    override suspend fun collectSamplingPreference(): Flow<SamplingSpeed> = flow {
        emit(dummySamplingSpeed.value)
    }

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {
        dummySamplingSpeed.value = samplingSpeed
    }

    override suspend fun startSharedFlowForDataCollectionFromHeartRateService() {
        // Do nothing.
    }
}