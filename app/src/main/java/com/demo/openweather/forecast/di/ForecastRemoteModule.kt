package com.demo.openweather.forecast.di


import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.demo.openweather.forecast.data.remote.source.ForecastRemoteDataSource
import com.demo.openweather.forecast.data.remote.source.ForecastRemoteDataSourceImpl
import com.demo.openweather.forecast.data.remote.services.ForecastService


@Module(includes = [ForecastRemoteModule.Binders::class])
class ForecastRemoteModule {
    @Module
    interface Binders {
        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: ForecastRemoteDataSourceImpl): ForecastRemoteDataSource
    }

    @Provides
    fun providesForecastService(retrofit: Retrofit): ForecastService = retrofit.create(ForecastService::class.java)


}