package com.demo.openweather.forecast.di

import com.demo.openweather.forecast.data.repository.ForecastRepository
import com.demo.openweather.forecast.data.repository.ForecastRepositoryImpl
import com.demo.openweather.forecast.domain.ForecastUseCaseImpl
import com.demo.openweather.forecast.domain.ForecastUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class ForecastDomainModule {

    @Binds
    abstract fun bindsRepository(
        repoImpl: ForecastRepositoryImpl
    ): ForecastRepository


    @Binds
    abstract fun bindsForecastUseCase(
        mForecastUseCase: ForecastUseCaseImpl
    ): ForecastUseCase


}