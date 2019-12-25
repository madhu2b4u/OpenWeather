package com.demo.openweather.forecast.data.remote.source

import com.demo.openweather.BuildConfig
import com.demo.openweather.di.qualifiers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import com.demo.openweather.forecast.data.remote.services.ForecastService
import com.demo.openweather.weather.data.model.WeatherResponse


class ForecastRemoteDataSourceImpl @Inject constructor(
    private val service: ForecastService,
    @IO private val context: CoroutineContext) : ForecastRemoteDataSource {


    override suspend fun getForecast(latitude: String, longitude: String) = withContext(context) {
        val response = service.getForecastAsync(latitude,longitude, BuildConfig.API_KEY).await()

        if (response.isSuccessful)
            response.body() ?: throw Exception("no city")
        else
            throw Exception("invalid request with code ${response.code()}")


    }


}
