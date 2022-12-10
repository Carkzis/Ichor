package com.carkzis.ichor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideHeartRateDao(database: IchorDatabase): HeartRateDao {
        return database.heartRateDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): IchorDatabase {
        return Room.databaseBuilder(
            context,
            IchorDatabase::class.java,
            "ichor"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideSamplingPreferenceDataStoreImpl(@ApplicationContext context: Context): SamplingPreferenceDataStore {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES)}
        )
        return SamplingPreferenceDataStoreImpl(dataStore = dataStore)
    }

    @Singleton
    @Provides
    fun provideRepository(database: IchorDatabase, heartRateService: HeartRateService, dataStore: SamplingPreferenceDataStore): Repository {
        return DefaultRepositoryImpl(database, heartRateService, dataStore)
    }
}
