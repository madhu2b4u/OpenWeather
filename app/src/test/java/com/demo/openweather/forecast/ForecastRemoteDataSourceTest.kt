package com.demo.openweather.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.openweather.BuildConfig
import com.demo.openweather.MainCoroutineRule
import com.demo.openweather.forecast.data.model.*
import com.demo.openweather.forecast.data.model.Clouds
import com.demo.openweather.forecast.data.model.Main
import com.demo.openweather.forecast.data.model.Sys
import com.demo.openweather.forecast.data.model.Weather
import com.demo.openweather.forecast.data.model.Wind
import com.demo.openweather.forecast.data.remote.services.ForecastService
import com.demo.openweather.forecast.data.remote.source.ForecastRemoteDataSource
import com.demo.openweather.forecast.data.remote.source.ForecastRemoteDataSourceImpl
import com.demo.openweather.weather.data.model.*
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class ForecastRemoteDataSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var remoteSource: ForecastRemoteDataSource

    lateinit var service: ForecastService

    val coord = Coord(12.45,17.89)
    val weather = arrayListOf<Weather>(Weather("desc","icon",788,"Drixxle"))
    val clouds = Clouds(11)
    val main = Main(280.1, 11, 19, 23, 33, 33.9,1.0,1.0,10.0)
    val wind = Wind(1,1.7)
    val dt =1191919
    val sys= Sys("IT")
    val id = 1
    val name = "Hawali"
    val city = City(coord,"India",id,name,100,1,1,1)
    val lstForecast = arrayListOf<Forecast>(Forecast(clouds,dt,"2019-09-11 18:00:00",main, Rain(11.0),sys,weather,wind))

    val latitude ="128.9"
    val longitude = "34.9"
    val forecastResponse = ForecastResponse(city,1,"1",lstForecast,dt)



    @Before
    fun init() {

        service = mock {
            onBlocking { getForecastAsync(latitude,longitude, BuildConfig.API_KEY) } doReturn GlobalScope.async {
                Response.success(forecastResponse)
            }
        }

        remoteSource = ForecastRemoteDataSourceImpl(service, mainCoroutineRule.coroutineContext)


    }

    @Test
    fun testGetForecast() = runBlocking {

        service = mock {
            onBlocking { getForecastAsync(latitude,longitude, BuildConfig.API_KEY) } doReturn GlobalScope.async {
                Response.success(forecastResponse)
            }
        }

        remoteSource = ForecastRemoteDataSourceImpl(service, mainCoroutineRule.coroutineContext)


        // Will be launched in the mainThreadSurrogate dispatcher
        val result = remoteSource.getForecast(latitude,longitude)



        assert(result == forecastResponse)


    }

    @Test(expected = Exception::class)
    fun testThrowArticleException() = runBlocking {

        service = mock {
            onBlocking { getForecastAsync(latitude,longitude, BuildConfig.API_KEY) } doReturn GlobalScope.async {
                Response.error<ForecastResponse>(404, null)
            }
        }
        remoteSource = ForecastRemoteDataSourceImpl(service, mainCoroutineRule.coroutineContext)

        val result = remoteSource.getForecast(latitude,longitude)




    }


}