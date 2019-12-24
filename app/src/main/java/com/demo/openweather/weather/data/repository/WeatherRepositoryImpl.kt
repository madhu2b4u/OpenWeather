package com.demo.openweather.weather.data.repository


import androidx.lifecycle.liveData
import javax.inject.Inject
import com.demo.openweather.common.Result
import com.demo.openweather.weather.data.remote.source.WeatherRemoteDataSource


class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getCityTemperature(city: String) = liveData {

        emit(Result.loading())
        try {
            val response = remoteDataSource.getCityTemperature(city)
            emit(Result.success(response))

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: ""))
        }
    }
}