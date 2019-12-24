package com.demo.openweather
import com.demo.openweather.di.DaggerWeatherAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class WeatherApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerWeatherAppComponent.builder().application(this).build()

    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this);
    }


}