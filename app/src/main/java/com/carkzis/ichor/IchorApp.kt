package com.carkzis.ichor

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class IchorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Set up Timber for logging.
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}