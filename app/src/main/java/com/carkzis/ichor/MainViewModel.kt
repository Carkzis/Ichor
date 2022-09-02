package com.carkzis.ichor

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _latestHeartRate = MutableStateFlow(0.0)
    val latestHeartRate: StateFlow<Double>
        get() = _latestHeartRate

    private val _latestHeartRateList = MutableStateFlow<List<DomainHeartRate>>(listOf())
    val latestHeartRateList: StateFlow<List<DomainHeartRate>>
        get() = _latestHeartRateList

    fun initiateDataCollection() {
        viewModelScope.launch {
            assignLatestHeartRateToUI()
        }
        viewModelScope.launch {
            assignLatestHeartRateListToUI()
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

    private suspend fun assignLatestHeartRateListToUI() {
        Timber.e("Entered assignLatestHeartRateListToUI.")
        repository.collectHeartRatesFromDatabase().collect { listOfHeartRates ->
            println(listOfHeartRates)
            _latestHeartRateList.value = listOfHeartRates
        }
    }
}