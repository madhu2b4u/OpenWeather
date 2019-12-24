package com.demo.openweather.weather.domain

import androidx.lifecycle.LiveData
import com.demo.openweather.common.Result
import com.demo.openweather.weather.data.model.WeatherResponse

interface WeatherFragmentUseCase {
    suspend fun getTemperature(city :String): LiveData<Result<WeatherResponse>>



}