package com.carkzis.ichor

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _latestHeartRate = MutableStateFlow(0.0)
    val latestHeartRate: StateFlow<Double>
        get() = _latestHeartRate

    suspend fun assignLatestHeartRateToUI() {
        repository.collectHeartRateFromHeartRateService().collect {
            val latestHeartRateAsDouble = it.last().value.asDouble()
            _latestHeartRate.value = latestHeartRateAsDouble
            Timber.e("Latest heart rate is $latestHeartRateAsDouble bpm")
        }
    }
}