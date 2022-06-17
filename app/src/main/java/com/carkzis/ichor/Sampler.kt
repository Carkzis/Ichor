package com.carkzis.ichor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class Sampler {

    fun sampleAtIntervals(intervalInMs: Long, initialIntervalInMs: Long = 0L) = flow {
        delay(initialIntervalInMs)
        while (true) {
            emit(Unit)
            delay(intervalInMs)
        }
    }

}