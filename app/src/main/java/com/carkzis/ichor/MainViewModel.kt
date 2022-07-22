package com.carkzis.ichor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _latestHeartRate = MutableStateFlow(0.0)
    val latestHeartRate: StateFlow<Double>
        get() = _latestHeartRate

    fun initiateDataCollection() {
        viewModelScope.launch {
            assignLatestHeartRateToUI()
        }
    }

    private suspend fun assignLatestHeartRateToUI() {
        Timber.e("Entered assignLatestHeartRateToUI.")
        repository.collectHeartRateFromHeartRateService().collect {
            val latestHeartRateAsDouble = it.last().value.asDouble()
            _latestHeartRate.value = latestHeartRateAsDouble
            Timber.e("Latest heart rate is $latestHeartRateAsDouble bpm.")
        }
    }
}