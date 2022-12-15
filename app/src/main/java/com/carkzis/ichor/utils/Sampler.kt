package com.carkzis.ichor.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import timber.log.Timber

//const val DEFAULT_INTERVAL_MS = 10_000L
//const val DEFAULT_INITIAL_INTERVAL_MS = 0L
//const val SLOW_INTERVAL_MS = 20_000L
//const val SLOW_INITIAL_INTERVAL_MS = 10_000L
//const val FAST_INTERVAL_MS = 5_000L
//const val FAST_INITIAL_INTERVAL_MS = 0L

sealed class Sampler(
    open val intervalInMs: Long = PostInitialSamplingIntervals.forSamplingSpeed(
        SamplingSpeed.DEFAULT
    ),
    open val initialIntervalInMs: Long = InitialSamplingIntervals.forSamplingSpeed(
        SamplingSpeed.DEFAULT
    )
) {

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
        if (intervalInMs < 0) {
            throw IllegalArgumentException("Only positive intervals allowed.")
        } else if (initialIntervalInMs < 0) {
            throw IllegalArgumentException("Only zero or positive initial intervals allowed.")
        }
    }
}

class DefaultSampler() : Sampler(
    PostInitialSamplingIntervals.forSamplingSpeed(SamplingSpeed.DEFAULT),
    InitialSamplingIntervals.forSamplingSpeed(SamplingSpeed.DEFAULT)
)

class SlowSampler() : Sampler(
    PostInitialSamplingIntervals.forSamplingSpeed(SamplingSpeed.SLOW),
    InitialSamplingIntervals.forSamplingSpeed(SamplingSpeed.SLOW)
)

class FastSampler() : Sampler(
    PostInitialSamplingIntervals.forSamplingSpeed(SamplingSpeed.FAST),
    InitialSamplingIntervals.forSamplingSpeed(SamplingSpeed.FAST)
)

class CustomSampler(
    override var intervalInMs: Long = PostInitialSamplingIntervals.forSamplingSpeed(
        SamplingSpeed.DEFAULT
    ),
    override var initialIntervalInMs: Long = InitialSamplingIntervals.forSamplingSpeed(
        SamplingSpeed.DEFAULT
    )
) : Sampler()

object InitialSamplingIntervals {
    private const val DEFAULT_INITIAL_INTERVAL_MS = 0L
    private const val SLOW_INITIAL_INTERVAL_MS = 10_000L
    private const val FAST_INITIAL_INTERVAL_MS = 0L

    fun forSamplingSpeed(samplingSpeed: SamplingSpeed): Long {
        return when (samplingSpeed) {
            SamplingSpeed.DEFAULT -> DEFAULT_INITIAL_INTERVAL_MS
            SamplingSpeed.SLOW -> SLOW_INITIAL_INTERVAL_MS
            SamplingSpeed.FAST -> FAST_INITIAL_INTERVAL_MS
            SamplingSpeed.UNKNOWN -> DEFAULT_INITIAL_INTERVAL_MS
        }
    }
}

object PostInitialSamplingIntervals {
    private const val DEFAULT_INTERVAL_MS = 10_000L
    private const val SLOW_INTERVAL_MS = 20_000L
    private const val FAST_INTERVAL_MS = 5_000L

    fun forSamplingSpeed(samplingSpeed: SamplingSpeed): Long {
        return when (samplingSpeed) {
            SamplingSpeed.DEFAULT -> DEFAULT_INTERVAL_MS
            SamplingSpeed.SLOW -> SLOW_INTERVAL_MS
            SamplingSpeed.FAST -> FAST_INTERVAL_MS
            SamplingSpeed.UNKNOWN -> DEFAULT_INTERVAL_MS
        }
    }
}


