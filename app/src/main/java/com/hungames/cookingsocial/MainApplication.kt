package com.hungames.cookingsocial.ui

import android.app.Application
import com.hungames.cookingsocial.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class MainApplication : Application()//, Configuration.Provider
{

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    /*
    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


     */

}