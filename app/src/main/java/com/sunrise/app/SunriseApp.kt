package com.sunrise.app

import android.app.Application
import com.sunrise.app.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SunriseApp @Inject constructor() : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}