package com.carkzis.ichor.tests.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carkzis.ichor.data.local.IchorDatabase
import com.carkzis.ichor.data.local.LocalHeartRate
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

        assertThat(heartRates.size, `is`(2))
        assertThat(heartRate1, `is`(heartRates[0]))
        assertThat(heartRate2, `is`(heartRates[1]))
    }

    @Test
    fun deleteLocalHeartRate_oneHeartRateDeleted_heartRateNoLongerExistsInDatabase() = runTest {
        val heartRateToDelete = LocalHeartRate(pk = "1", date = "01/01/1900", value = "100")

        database.heartRateDao().insertHeartRate(heartRate = heartRateToDelete)
        database.heartRateDao().deleteLocalHeartRate("1")

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        assertThat(heartRates.size, `is`(0))
    }

    @Test
    fun deleteLocalHeartRate_heartRateDoesNotExistInDatabase_noChangeToDatabase() = runTest {
        val heartRateToDelete = LocalHeartRate(pk = "1", date = "01/01/1900", value = "100")

        database.heartRateDao().insertHeartRate(heartRate = heartRateToDelete)
        database.heartRateDao().deleteLocalHeartRate("2")

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        assertThat(heartRates.size, `is`(1))
    }

    @Test
    fun deleteAllHeartRates_onAllHeartRatesDeleted_noHeartRatesWithinDatabase() = runTest {
        val allHeartRates = listOf(
            LocalHeartRate(pk = "1", date = "01/01/1900", value = "100"),
            LocalHeartRate(pk = "2", date = "01/01/1900", value = "200"),
            LocalHeartRate(pk = "3", date = "01/01/1900", value = "300")
        )

        allHeartRates.forEach {
            database.heartRateDao().insertHeartRate(it)
        }

        database.heartRateDao().deleteAllLocalHeartRates()

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        assertThat(heartRates.size, `is`(0))
    }

    @Test
    fun deleteAllHeartRates_onAttemptDeleteAllHeartRatesFromEmptyDatabase_noHeartRatesWithinDatabase() = runTest {
        database.heartRateDao().deleteAllLocalHeartRates()

        val heartRates = database
            .heartRateDao()
            .getAllLocalHeartRates()
            .take(1)
            .toList()[0]

        assertThat(heartRates.size, `is`(0))
    }

}
