package com.demo.openweather.forecast.data.remote.source

import com.demo.openweather.forecast.data.model.ForecastResponse


interface ForecastRemoteDataSource {
    suspend fun getForecast(latitude :String, longitude :String): ForecastResponse

}