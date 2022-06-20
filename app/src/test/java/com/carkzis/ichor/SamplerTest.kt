package com.carkzis.ichor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis
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
        sut?.let {
            it.sampleAtIntervals(100)
                .first()
                .run {
                    assertTrue("A value was emitted.", true)
                    return@runBlocking
                }
        }

        assertTrue("A value was not emitted.", false)
    }

    @Test
    fun `samples emits 10 values in 10 intervals`() = runBlocking {
        val counter = AtomicInteger(0)
        val intervalInMs = 100L
        val initialIntervalInMs = 0L
        val intervals = 10

        val elapsedTimeInMs = measureTimeMillis {
            sut?.let {
                it.sampleAtIntervals(intervalInMs, initialIntervalInMs)
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertCorrectAmountOfEmissions(intervals, counter.get())
        assertCorrectAmountOfTimeElapsed(elapsedTimeInMs, intervals, intervalInMs, initialIntervalInMs)
    }

    @Test
    fun `samples emit 1 value after initial interval of 1 seconds`() = runBlocking {
        val counter = AtomicInteger(0)
        val intervalInMs = 150L
        val initialIntervalInMs = 1000L
        val intervals = 1

        val elapsedTimeInMs = measureTimeMillis {
            sut?.let {
                it.sampleAtIntervals(intervalInMs, initialIntervalInMs)
                    .take(intervals)
                    .collect {
                        counter.incrementAndGet()
                    }
            }
        }

        assertCorrectAmountOfEmissions(intervals, counter.get())
        assertCorrectAmountOfTimeElapsed(elapsedTimeInMs, intervals, intervalInMs, initialIntervalInMs)
    }

    // TODO: Negative intervals.

    private fun assertCorrectAmountOfTimeElapsed(
        elapsedTimeInMs: Long,
        intervals: Int,
        intervalInMs: Long,
        initialIntervalInMs: Long
    ) {

        val expectedLowerBoundInMs =
            getExpectedLowerBoundsInMs(intervals, intervalInMs, initialIntervalInMs)
        val expectedUpperBoundInMs = getExpectedUpperBoundsInMs(expectedLowerBoundInMs)

        assertTrue(
            "Elapsed time was $elapsedTimeInMs, lower bound was $expectedLowerBoundInMs, " +
                    "upper bound was $expectedUpperBoundInMs",
            elapsedTimeInMs in expectedLowerBoundInMs..expectedUpperBoundInMs
        )
    }

    private fun getExpectedLowerBoundsInMs(
        intervals: Int,
        intervalInMs: Long,
        initialIntervalInMs: Long
    ) =
        ((intervals - 1) * intervalInMs) + initialIntervalInMs

    private fun getExpectedUpperBoundsInMs(expectedLowerBounds: Long, multiplier: Double = 1.2) =
        (expectedLowerBounds * multiplier).toInt()

    private fun assertCorrectAmountOfEmissions(expectedEmissions: Int, actualEmissions: Int) {
        assertTrue("Value was $actualEmissions", expectedEmissions == actualEmissions)
    }

}