package com.carkzis.ichor

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

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
    fun provideRepository(heartRateService: HeartRateService): Repository {
        return DefaultRepositoryImpl(heartRateService)
    }
}