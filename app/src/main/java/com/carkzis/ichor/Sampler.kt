package com.carkzis.ichor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

const val DEFAULT_INTERVAL_MS = 1_000L
const val DEFAULT_INITIAL_INTERVAL_MS = 0L

class Sampler(val intervalInMs: Long = DEFAULT_INTERVAL_MS, val initialIntervalInMs: Long = DEFAULT_INITIAL_INTERVAL_MS) {

    fun sampleAtIntervals() = flow {
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