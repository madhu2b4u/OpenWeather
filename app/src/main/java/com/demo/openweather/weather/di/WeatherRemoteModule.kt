package com.demo.openweather.weather.di


import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.demo.openweather.weather.data.remote.source.WeatherRemoteDataSource
import com.demo.openweather.weather.data.remote.source.WeatherRemoteDataSourceImpl
import com.demo.openweather.weather.data.remote.services.WeatherService


@Module(includes = [WeatherRemoteModule.Binders::class])
class WeatherRemoteModule {


    @Module
    interface Binders {


        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: WeatherRemoteDataSourceImpl
        ): WeatherRemoteDataSource


    }


    @Provides
    fun providesWeatherService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)


}