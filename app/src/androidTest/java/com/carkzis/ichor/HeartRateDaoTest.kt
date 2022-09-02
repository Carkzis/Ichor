package com.carkzis.ichor

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HeartRateDaoTest {

    private lateinit var database: IchorDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            IchorDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertHeartRate_insertOneHeartRate_retrieveInsertedHeartRate() = runTest {
        val heartRate = LocalHeartRate(pk = "1", date = "01/01/1900", value = "100")

        database.heartRateDao().insertHeartRate(heartRate = heartRate)

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        assertThat(heartRates.size, `is`(1))
        assertThat(heartRate, `is`(heartRates[0]))
    }

    @Test
    fun insertHeartRate_insertDuplicateHeartRate_retrieveOnlyOneOfHeartRateFromDatabase() = runTest {
        val heartRate1 = LocalHeartRate(pk = "1", date = "01/01/1900", value = "100")
        val heartRate2 = LocalHeartRate(pk = "1", date = "01/01/1900", value = "100")

        database.heartRateDao().insertHeartRate(heartRate = heartRate1)
        database.heartRateDao().insertHeartRate(heartRate = heartRate2)

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        assertThat(heartRates.size, `is`(1))
        assertThat(heartRate1, `is`(heartRates[0]))
    }

    @Test
    fun insertHeartRate_insertTwoDifferentHeartRates_retrieveTwoDifferentHeartRates() = runTest {
        val heartRate1 = LocalHeartRate(pk = "1", date = "01/01/1900", value = "100")
        val heartRate2 = LocalHeartRate(pk = "2", date = "01/01/1900", value = "200")

        database.heartRateDao().insertHeartRate(heartRate = heartRate1)
        database.heartRateDao().insertHeartRate(heartRate = heartRate2)

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        println(heartRates)
        assertThat(heartRates.size, `is`(2))
        assertThat(heartRate1, `is`(heartRates[0]))
        assertThat(heartRate2, `is`(heartRates[1]))
    }

}
