package com.sample.playground

import android.app.Application
import com.sample.playground.di.appModule
import com.sample.playground.di.module.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, networkModule))
        }
    }
}