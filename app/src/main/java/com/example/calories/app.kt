package com.example.calories

import android.app.Application
import com.example.calories.domain.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class app: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@app)
            modules(appModule)
        }
    }
}