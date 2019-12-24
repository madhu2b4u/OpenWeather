package com.demo.openweather.di

import android.app.Application
import com.demo.openweather.WeatherApp
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
        AppModule::class
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