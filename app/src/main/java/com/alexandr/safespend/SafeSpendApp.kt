package com.alexandr.safespend

import android.app.Application
import com.alexandr.safespend.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SafeSpendApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SafeSpendApp)
            modules(appModules())
        }
    }
}

