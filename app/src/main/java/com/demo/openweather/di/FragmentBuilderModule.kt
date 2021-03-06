package com.demo.openweather.di

import com.demo.openweather.forecast.presentation.ui.fragments.ForecastFragment
import com.demo.openweather.weather.presentation.ui.fragments.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuilderModule {


    @ContributesAndroidInjector
    abstract fun contributesWeatherFragment(): WeatherFragment

    @ContributesAndroidInjector
    abstract fun contributesForecastFragment(): ForecastFragment
}