package com.carkzis.ichor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
internal class SamplerTest {

    var sut: Sampler? = null

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `sampler emits a value when sampleAtIntervals is called`() = runBlocking {
        sut = CustomSampler(intervalInMs = 100)
        sut?.run {
            sampleAtIntervals()
                .first()
                .run {
                    return@runBlocking
                }
        }

        fail("A value was not emitted.")
    }

    @Test
    fun `sampler emit 1 value after initial interval of 1 seconds`() = runTest {
        val counter = AtomicInteger(0)
        val intervalInMs = 150L
        val initialIntervalInMs = 1000L
        val intervals = 1
        sut = CustomSampler(intervalInMs, initialIntervalInMs)

        launch {
            sut?.run {
                sampleAtIntervals()
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertThat(counter.get(), `is`(0))
        runCurrent()
        advanceTimeBy(initialIntervalInMs + 1)
        assertThat(counter.get(), `is`(1))
        assertThat(counter.get(), `is`(intervals))

    }

    @Test
    fun `sampler emits 10 values in 10 intervals`() = runTest {
        val counter = AtomicInteger(0)
        val intervalInMs = 100L
        val initialIntervalInMs = 0L
        val intervals = 10
        sut = CustomSampler(intervalInMs, initialIntervalInMs)

        launch {
            sut?.run {
                sampleAtIntervals()
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertThat(counter.get(), `is`(0))
        runCurrent()
        advanceTimeBy(initialIntervalInMs + 1)
        assertThat(counter.get(), `is`(1))
        advanceTimeBy(intervalInMs)
        assertThat(counter.get(), `is`(2))
        advanceTimeBy((intervalInMs * (intervals - 2)))
        assertThat(counter.get(), `is`(intervals))

    }

    @Test(expected = IllegalArgumentException::class)
    fun `sampler throws exception if repeated interval provided negative value`(): Unit = runBlocking {
        val counter = AtomicInteger(0)
        val intervalInMs = -100L
        val initialIntervalInMs = 0L
        val intervals = 10
        sut = CustomSampler(intervalInMs, initialIntervalInMs)

        sut?.run {
            sampleAtIntervals()
                .take(intervals)
                .collect {
                    counter.incrementAndGet()
                }
        }

    }

    @Test(expected = IllegalArgumentException::class)
    fun `sampler throws exception if initial interval provided negative value`() = runTest {
        val counter = AtomicInteger(0)
        val intervalInMs = 100L
        val initialIntervalInMs = -100L
        val intervals = 10
        sut = CustomSampler(intervalInMs, initialIntervalInMs)

        sut?.run {
            sampleAtIntervals()
                .take(intervals)
                .collect {
                    counter.incrementAndGet()
                }
        }

    }

    @Test
    fun `slow sampler samples at rate expected`() = runTest {
        val counter = AtomicInteger(0)
        val intervals = 10
        sut = SlowSampler()

        launch {
            sut?.run {
                sampleAtIntervals()
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertThat(counter.get(), `is`(0))
        runCurrent()
        // Need to add 1ms to allow time counter to increment.
        sut?.initialIntervalInMs?.let { advanceTimeBy(it + 1) }
        assertThat(counter.get(), `is`(1))
        sut?.intervalInMs?.let { advanceTimeBy(it) }
        assertThat(counter.get(), `is`(2))
        sut?.intervalInMs?.let { advanceTimeBy((it * (intervals - 2))) }
        assertThat(counter.get(), `is`(intervals))
    }

    @Test
    fun `default sampler samples at rate expected`() = runTest {
        val counter = AtomicInteger(0)
        val intervals = 10
        sut = DefaultSampler()

        launch {
            sut?.run {
                sampleAtIntervals()
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertThat(counter.get(), `is`(0))
        runCurrent()
        // Need to add 1ms to allow time counter to increment.
        sut?.initialIntervalInMs?.let { advanceTimeBy(it + 1) }
        assertThat(counter.get(), `is`(1))
        sut?.intervalInMs?.let { advanceTimeBy(it) }
        assertThat(counter.get(), `is`(2))
        sut?.intervalInMs?.let { advanceTimeBy((it * (intervals - 2))) }
        assertThat(counter.get(), `is`(intervals))
    }

    @Test
    fun `fast sampler samples at rate expected`() = runTest {
        val counter = AtomicInteger(0)
        val intervals = 10
        sut = DefaultSampler()

        launch {
            sut?.run {
                sampleAtIntervals()
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertThat(counter.get(), `is`(0))
        runCurrent()
        // Need to add 1ms to allow time counter to increment.
        sut?.initialIntervalInMs?.let { advanceTimeBy(it + 1) }
        assertThat(counter.get(), `is`(1))
        sut?.intervalInMs?.let { advanceTimeBy(it) }
        assertThat(counter.get(), `is`(2))
        sut?.intervalInMs?.let { advanceTimeBy((it * (intervals - 2))) }
        assertThat(counter.get(), `is`(intervals))
    }

}