package com.demo.openweather.forecast.domain

import androidx.lifecycle.LiveData
import com.demo.openweather.common.Result
import com.demo.openweather.forecast.data.model.ForecastResponse

interface ForecastUseCase {
    suspend fun getForecast(latitude :String, longitude :String): LiveData<Result<ForecastResponse>>

}