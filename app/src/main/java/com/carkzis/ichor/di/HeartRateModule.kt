package com.carkzis.ichor.di

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import com.carkzis.ichor.data.heartrates.HeartRateCallbackProxy
import com.carkzis.ichor.data.heartrates.HeartRateCallbackProxyImpl
import com.carkzis.ichor.data.heartrates.HeartRateService
import com.carkzis.ichor.data.heartrates.HeartRateServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HeartRateModule {
    @Singleton
    @Provides
    fun provideHealthServiceClient(@ApplicationContext context: Context): HealthServicesClient {
        return HealthServices.getClient(context)
    }

    @Singleton
    @Provides
    fun provideHeartRateCallbackProxy() : HeartRateCallbackProxy {
        return HeartRateCallbackProxyImpl()
    }

    @Singleton
    @Provides
    fun provideHeartRateService(healthServicesClient: HealthServicesClient, heartRateCallbackProxy: HeartRateCallbackProxy): HeartRateService {
        return HeartRateServiceImpl(healthServicesClient, heartRateCallbackProxy)
    }
}