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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.util.*

class DummyRepository : Repository {
    private var dummyHasSamplingSpeed = MutableStateFlow(SamplingSpeed.DEFAULT)

    override suspend fun collectAvailabilityFromHeartRateService(): Flow<Availability> = flow {
        emit(DataTypeAvailability.UNKNOWN)
    }

    override suspend fun collectHeartRatesFromDatabase(): Flow<List<DomainHeartRate>> = flow {
        emit(listOf(
            LocalHeartRate(
            date = "2022-12-25T12:30:30.303",
            pk = "1",
            value = "100.0"
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
        emit(dummyHasSamplingSpeed.value)
    }

    override suspend fun changeSamplingPreference(samplingSpeed: SamplingSpeed) {
        dummyHasSamplingSpeed.value = samplingSpeed
    }

    override suspend fun startSharedFlowForDataCollectionFromHeartRateService() {
        // Do nothing.
    }

    private fun insertValueIntoDatabase(heartRate: DataPoint) {
        // Do nothing.
    }
}