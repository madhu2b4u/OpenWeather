package com.demo.openweather.weather.data.remote.services

import com.demo.openweather.weather.data.model.WeatherResponse
import retrofit2.Response
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {


    @GET("weather?")
    fun getCityTemperature(@Query("q") city: String, @Query("APPID") apiKey: String): Deferred<Response<WeatherResponse>>

}