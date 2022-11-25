package com.carkzis.ichor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import timber.log.Timber

const val DEFAULT_INTERVAL_MS = 10_000L
const val DEFAULT_INITIAL_INTERVAL_MS = 0L
const val SLOW_INTERVAL_MS = 20_000L
const val SLOW_INITIAL_INTERVAL_MS = 10_000L
const val FAST_INTERVAL_MS = 5_000L
const val FAST_INITIAL_INTERVAL_MS = 0L

sealed class Sampler(open val intervalInMs: Long = DEFAULT_INTERVAL_MS, open val initialIntervalInMs: Long = DEFAULT_INITIAL_INTERVAL_MS) {

    fun sampleAtIntervals() = flow {
        checkIntervalValues(intervalInMs, initialIntervalInMs)
        delay(initialIntervalInMs)
        Timber.e("Time to set up the sampler.")
        while (true) {
            Timber.e("Time to emit.")
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

class DefaultSampler() : Sampler(DEFAULT_INTERVAL_MS, DEFAULT_INITIAL_INTERVAL_MS)
class SlowSampler() : Sampler(SLOW_INTERVAL_MS, SLOW_INITIAL_INTERVAL_MS)
class FastSampler() : Sampler(FAST_INTERVAL_MS, FAST_INITIAL_INTERVAL_MS)
class CustomSampler(override var intervalInMs: Long = DEFAULT_INTERVAL_MS, override var initialIntervalInMs: Long = DEFAULT_INITIAL_INTERVAL_MS) : Sampler()
