package com.jotangi.NumberHealthy

import android.app.Application
import com.jotangi.NumberHealthy.api.AppClientManager
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.instances.init(this)
        WatchUtils.instance.initWatchSDK(this)
        WatchUtils.instance.deviceToApp()
        AppClientManager.instance.init()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
            modules(appModule2)
        }
    }
}