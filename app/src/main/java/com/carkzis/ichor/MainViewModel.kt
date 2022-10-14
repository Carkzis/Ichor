package com.carkzis.ichor

import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.proto.DataProto
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: DefaultRepositoryImpl) : ViewModel() {

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

    fun initiateDataCollection() {
        viewModelScope.launch {
            assignLatestHeartRateToUI()
        }
        viewModelScope.launch {
            assignLatestAvailabilityToUI()
        }
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                assignLatestHeartRateListToUI()
            }
        }
    }

    private suspend fun assignLatestHeartRateToUI() {
        Timber.e("Entered assignLatestHeartRateToUI.")
        val sampler = Sampler()
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
}