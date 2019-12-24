package com.demo.openweather.weather.data.remote.source

import com.demo.openweather.weather.data.model.WeatherResponse


interface WeatherRemoteDataSource {

    suspend fun getCityTemperature(city :String):WeatherResponse


}