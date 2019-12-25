package com.demo.openweather.weather.data.remote.services

import com.demo.openweather.weather.data.model.WeatherResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {


    @GET("weather?")
    fun getCityTemperatureAsync(@Query("q") city: String, @Query("APPID") apiKey: String): Deferred<Response<WeatherResponse>>

}