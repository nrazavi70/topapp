package com.test.topapp.framework

import android.app.Application
import com.test.topapp.framework.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TopAppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@TopAppApplication)
            modules(
                repos,
                useCases,
                databaseModule,
                serverModule,
                viewModelModule
            )
        }
    }
}