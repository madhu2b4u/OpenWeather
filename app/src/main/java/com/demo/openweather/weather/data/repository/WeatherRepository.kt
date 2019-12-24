package com.demo.openweather.weather.data.repository

import androidx.lifecycle.LiveData
import com.demo.openweather.common.Result
import com.demo.openweather.weather.data.model.WeatherResponse


interface WeatherRepository {

    suspend fun getCityTemperature(city :String): LiveData<Result<WeatherResponse>>


}