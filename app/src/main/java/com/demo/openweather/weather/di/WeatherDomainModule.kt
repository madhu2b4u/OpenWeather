package com.demo.openweather.weather.di

import com.demo.openweather.weather.data.repository.WeatherRepository
import com.demo.openweather.weather.data.repository.WeatherRepositoryImpl
import com.demo.openweather.weather.domain.WeatherFragmentUseCaseImpl
import com.demo.openweather.weather.domain.WeatherFragmentUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class WeatherDomainModule {

    @Binds
    abstract fun bindsRepository(
        repoImpl: WeatherRepositoryImpl
    ): WeatherRepository


    @Binds
    abstract fun bindsArticlesUseCase(
        mWeatherFragmentUseCase: WeatherFragmentUseCaseImpl
    ): WeatherFragmentUseCase


}