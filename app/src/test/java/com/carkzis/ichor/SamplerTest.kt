package com.carkzis.ichor

import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

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
    fun sampler_emitsAValue() = runBlocking {
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
}