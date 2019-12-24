package com.demo.openweather.weather.data.remote.source

import com.demo.openweather.BuildConfig
import com.demo.openweather.di.qualifiers.IO
import com.demo.openweather.weather.data.remote.services.WeatherService
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val service: WeatherService,
    @IO private val context: CoroutineContext) : WeatherRemoteDataSource {

    override suspend fun getCityTemperature(city :String) = withContext(context) {
        val response = service.getCityTemperature(city,BuildConfig.API_KEY).await()

        if (response.isSuccessful)
            response.body() ?: throw Exception("no city")
        else
            throw Exception("invalid request with code ${response.code()}")


    }

}
