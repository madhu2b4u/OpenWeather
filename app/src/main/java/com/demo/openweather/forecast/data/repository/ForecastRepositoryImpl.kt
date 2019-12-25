package com.demo.openweather.forecast.data.repository


import androidx.lifecycle.liveData
import com.demo.openweather.common.Result
import com.demo.openweather.forecast.data.remote.source.ForecastRemoteDataSource
import javax.inject.Inject


class ForecastRepositoryImpl @Inject constructor(
    private val remoteDataSource: ForecastRemoteDataSource) : ForecastRepository {


    override suspend fun getForecast(
        latitude: String,
        longitude: String) = liveData {

        emit(Result.loading())
        try {
            val response = remoteDataSource.getForecast(latitude,longitude)
            emit(Result.success(response))

        } catch (exception: Exception) {
            emit(Result.error(exception.message ?: ""))
        }
    }


}