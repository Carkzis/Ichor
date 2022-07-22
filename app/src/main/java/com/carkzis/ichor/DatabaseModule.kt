package com.carkzis.ichor

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    fun provideHealthServiceClient(@ApplicationContext context: Context): HealthServicesClient {
        return HealthServices.getClient(context)
    }

    @Singleton
    @Provides
    fun provideHeartRateService(healthServicesClient: HealthServicesClient): HeartRateService {
        return HeartRateServiceImpl(healthServicesClient)
    }

    @Singleton
    @Provides
    fun provideRepository(database: IchorDatabase, heartRateService: HeartRateService): Repository {
        return DefaultRepositoryImpl(database, heartRateService)
    }
}