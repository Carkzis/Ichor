package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val listOfJobs = mutableListOf<Job>()

    private val _latestHeartRate = MutableStateFlow(0.0)
    val latestHeartRate: StateFlow<Double>
        get() = _latestHeartRate

    private val _latestAvailability : MutableStateFlow<Availability> = MutableStateFlow(
        DataTypeAvailability.UNKNOWN
    )
    val latestAvailability: StateFlow<Availability>
        get() = _latestAvailability

    private val _latestHeartRateList = MutableStateFlow<List<DomainHeartRate>>(listOf())
    val latestHeartRateList: StateFlow<List<DomainHeartRate>>
        get() = _latestHeartRateList

    private val _currentSampleSpeed = MutableStateFlow<SamplingSpeed>(SamplingSpeed.DEFAULT)
    val currentSamplingSpeed: StateFlow<SamplingSpeed>
        get() = _currentSampleSpeed

    fun initiateDataCollection(samplingSpeed: SamplingSpeed = SamplingSpeed.DEFAULT) {
        listOfJobs.addAll(listOf(viewModelScope.launch {
            assignLatestHeartRateToUI(chooseSampler(samplingSpeed))
        },
        viewModelScope.launch {
            assignLatestAvailabilityToUI()
        },
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                assignLatestHeartRateListToUI()
            }
        }))
    }

    private fun chooseSampler(samplingSpeed: SamplingSpeed) : Sampler {
        return when (samplingSpeed) {
            SamplingSpeed.SLOW -> {
                SlowSampler()
            }
            SamplingSpeed.DEFAULT -> {
                DefaultSampler()
            }
            SamplingSpeed.FAST -> {
                FastSampler()
            }
        }
    }

    private suspend fun assignLatestHeartRateToUI(sampler: Sampler) {
        Timber.e("Entered assignLatestHeartRateToUI.")
        repository.collectHeartRateFromHeartRateService(sampler).collect {
            val latestHeartRateAsDouble = it.value.asDouble()
            _latestHeartRate.value = latestHeartRateAsDouble
            Timber.e("Latest heart rate is $latestHeartRateAsDouble bpm.")
        }
    }

    private suspend fun assignLatestAvailabilityToUI() {
        Timber.e("Entered assignLatestAvailabilityToUI.")
        repository.collectAvailabilityFromHeartRateService().collect { availability ->
//            Timber.e("Latest availability is $availability.")
            _latestAvailability.value = availability
        }
    }

    private suspend fun assignLatestHeartRateListToUI() {
        Timber.e("Entered assignLatestHeartRateListToUI.")
        repository.collectHeartRatesFromDatabase().collect { listOfHeartRates ->
//            Timber.e("Latest heart rates are $listOfHeartRates.")
            _latestHeartRateList.value = listOfHeartRates
        }
    }

    fun deleteHeartRate(primaryKey: String) {
        viewModelScope.launch {
            repository.deleteHeartRateFromDatabase(primaryKey)
        }
    }

    fun deleteAllHeartRates() {
        viewModelScope.launch {
            repository.deleteAllHeartRatesFromDatabase()
        }
    }

    fun changeSampleRate(samplerRate: SamplingSpeed) {
        listOfJobs.forEach { it.cancel() }
        listOfJobs.clear()
        _currentSampleSpeed.value = samplerRate
        initiateDataCollection(samplerRate)
    }
}

enum class SamplingSpeed {
    SLOW,
    DEFAULT,
    FAST
}