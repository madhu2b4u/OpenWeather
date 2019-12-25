package com.demo.openweather.forecast.data.remote.services

import com.demo.openweather.forecast.data.model.ForecastResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastService {
    @GET("forecast?")
    fun getForecastAsync(@Query("lat") lat: String, @Query("lon")lon : String, @Query("APPID") apiKey: String): Deferred<Response<ForecastResponse>>

}