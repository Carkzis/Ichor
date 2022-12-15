package com.carkzis.ichor.testdoubles

import androidx.health.services.client.data.*
import com.carkzis.ichor.data.*
import com.carkzis.ichor.ui.SamplingSpeed
import com.carkzis.ichor.utils.Sampler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.util.*

class DummyRepository : Repository {

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> = flow {
        emit(DataTypeAvailability.UNKNOWN)
    }

    override suspend fun collectHeartRatesFromDatabase(): Flow<List<DomainHeartRate>> = flow {
        emit(listOf(
            LocalHeartRate(
            date = "",
            pk = "",
            value = ""
        )
        ).toDomainHeartRate())
    }

    override suspend fun deleteHeartRateFromDatabase(primaryKey: String) {}

    override suspend fun deleteAllHeartRatesFromDatabase() {
    }

    override suspend fun collectHeartRateFromHeartRateService(sampler: Sampler): Flow<HeartRateDataPoint> =
        flow {
            emit(DataPoint.createSample(
                DataType.HEART_RATE_BPM,
                Value.ofDouble(0.0),
                Duration.ofSeconds(0)
            ))
        }

    override suspend fun collectSamplingPreference(): Flow<SamplingSpeed> = flow {
        emit(SamplingSpeed.DEFAULT)
    }

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {

    }

    override suspend fun startSharedFlowForDataCollectionFromHeartRateService() {
        // Do nothing.
    }

    private fun insertValueIntoDatabase(heartRate: DataPoint) {

    }
}