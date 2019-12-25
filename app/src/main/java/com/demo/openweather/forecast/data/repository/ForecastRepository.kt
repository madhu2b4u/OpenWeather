package com.demo.openweather.forecast.data.repository

import androidx.lifecycle.LiveData
import com.demo.openweather.common.Result
import com.demo.openweather.forecast.data.model.ForecastResponse


interface ForecastRepository {

    suspend fun getForecast(latitude:String, longitude :String): LiveData<Result<ForecastResponse>>


}