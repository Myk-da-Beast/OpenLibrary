package com.myk.openlibrary

import android.app.Application
import io.realm.Realm
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        Realm.init(this)

        // Start Koin
        startKoin(this, modules, logger = AndroidLogger())
    }
}