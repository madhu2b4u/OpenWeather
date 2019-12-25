package com.demo.openweather.di

import android.app.Application
import com.demo.openweather.WeatherApp
import com.demo.openweather.forecast.di.ForecastDomainModule
import com.demo.openweather.forecast.di.ForecastPresentationModule
import com.demo.openweather.forecast.di.ForecastRemoteModule
import com.demo.openweather.weather.di.WeatherDomainModule
import com.demo.openweather.weather.di.WeatherPresentationModule
import com.demo.openweather.weather.di.WeatherRemoteModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        NetworkModule::class,
        FragmentBuilderModule::class,
        ActivityBuilderModule::class,
        WeatherDomainModule::class,
        WeatherPresentationModule::class,
        WeatherRemoteModule::class,
        AppModule::class,
        ForecastDomainModule::class,
        ForecastPresentationModule::class,ForecastRemoteModule::class
    ]
)
interface WeatherAppComponent : AndroidInjector<WeatherApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): WeatherAppComponent
    }

    override fun inject(app: WeatherApp)
}