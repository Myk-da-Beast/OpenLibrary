package com.myk.openlibrary

import android.app.Application
import com.myk.openlibrary.dependencyInjection.modules
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        // Start Koin
        startKoin(this, modules, logger = AndroidLogger())
    }
}