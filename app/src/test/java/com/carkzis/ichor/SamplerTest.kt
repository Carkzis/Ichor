package com.carkzis.ichor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
internal class SamplerTest {

    var sut: Sampler? = null

    @Before
    fun setUp() {
        sut = Sampler()
    }

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun `sampler emits a value when sampleAtIntervals is called`() = runBlocking {
        sut?.run {
            sampleAtIntervals(100)
                .first()
                .run {
                    assertTrue("A value was emitted.", true)
                    return@runBlocking
                }
        }

        assertTrue("A value was not emitted.", false)
    }

    @Test
    fun `sampler emit 1 value after initial interval of 1 seconds`() = runTest {
        val counter = AtomicInteger(0)
        val intervalInMs = 150L
        val initialIntervalInMs = 1000L
        val intervals = 1

        launch {
            sut?.run {
                sampleAtIntervals(intervalInMs, initialIntervalInMs)
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertEquals(0, counter.get())
        runCurrent()
        advanceTimeBy(initialIntervalInMs + 1)
        assertEquals(1, counter.get())
        assertEquals(intervals, counter.get())
    }

    @Test
    fun `sampler emits 10 values in 10 intervals`() = runTest {
        val counter = AtomicInteger(0)
        val intervalInMs = 100L
        val initialIntervalInMs = 0L
        val intervals = 10

        launch {
            sut?.run {
                sampleAtIntervals(intervalInMs, initialIntervalInMs)
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertEquals(0, counter.get())
        runCurrent()
        advanceTimeBy(initialIntervalInMs + 1)
        assertEquals(1, counter.get())
        advanceTimeBy(intervalInMs)
        assertEquals(2, counter.get())
        advanceTimeBy((intervalInMs * (intervals - 2)))
        assertEquals(intervals, counter.get())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `sampler throws exception if repeated interval provided negative value`(): Unit = runBlocking {
        val counter = AtomicInteger(0)
        val intervalInMs = -100L
        val initialIntervalInMs = 0L
        val intervals = 10

        sut?.run {
            sampleAtIntervals(intervalInMs, initialIntervalInMs)
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

        sut?.run {
            sampleAtIntervals(intervalInMs, initialIntervalInMs)
                .take(intervals)
                .collect {
                    counter.incrementAndGet()
                }
        }

    }

}