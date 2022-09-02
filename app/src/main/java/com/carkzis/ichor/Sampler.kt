package com.carkzis.ichor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class Sampler() {

    fun sampleAtIntervals(intervalInMs: Long, initialIntervalInMs: Long = 0L) = flow {
        checkIntervalValues(intervalInMs, initialIntervalInMs)
        delay(initialIntervalInMs)
        while (true) {
            emit(Unit)
            delay(intervalInMs)
        }
    }

    private fun checkIntervalValues(intervalInMs: Long, initialIntervalInMs: Long = 0L) {
        if (intervalInMs <= 0) {
            throw IllegalArgumentException("Only positive intervals allowed.")
        } else if (initialIntervalInMs < 0) {
            throw IllegalArgumentException("Only zero or positive initial intervals allowed.")
        }
    }

}