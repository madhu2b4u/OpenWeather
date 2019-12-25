package com.demo.openweather.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.openweather.BuildConfig
import com.demo.openweather.MainCoroutineRule
import com.demo.openweather.weather.data.model.*
import com.demo.openweather.weather.data.remote.services.WeatherService
import com.demo.openweather.weather.data.remote.source.WeatherRemoteDataSource
import com.demo.openweather.weather.data.remote.source.WeatherRemoteDataSourceImpl
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
class WeatherRemoteDataSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    lateinit var weatherService: WeatherService

    val coord = Cordinates(12.45,17.89)
    val weather = arrayListOf<Weather>(Weather("desc","icon",788,"Drixxle"))
    val clouds = Clouds(11)
    val visibility = 10000
    val main = Main(280.1,11,19,23.0,33.9,33.9)
    val wind = Wind(1,1.7)
    val dt =1191919
    val sys= Sys("IT",6756,1,1,1)
    val timezone= 3600
    val id = 1
    val name = "Hawali"
    val cod= 200
    val weatherResponse = WeatherResponse("stations",clouds,cod,coord,dt,id,main,name,sys,timezone,visibility,weather,wind)

    val cityName = "Hyderabad"


    @Before
    fun init() {

        weatherService = mock {
            onBlocking { getCityTemperatureAsync(cityName, BuildConfig.API_KEY) } doReturn GlobalScope.async {
                Response.success(weatherResponse)
            }
        }

        weatherRemoteDataSource = WeatherRemoteDataSourceImpl(weatherService, mainCoroutineRule.coroutineContext)


    }

    @Test
    fun testGetWeather() = runBlocking {

        weatherService = mock {
            onBlocking { getCityTemperatureAsync(cityName, BuildConfig.API_KEY) } doReturn GlobalScope.async {
                Response.success(weatherResponse)
            }
        }

        weatherRemoteDataSource = WeatherRemoteDataSourceImpl(weatherService, mainCoroutineRule.coroutineContext)


        // Will be launched in the mainThreadSurrogate dispatcher
        val result = weatherRemoteDataSource.getCityTemperature(cityName)



        assert(result == weatherResponse)


    }

    @Test(expected = Exception::class)
    fun testThrowWeatherException() = runBlocking {

        weatherService = mock {
            onBlocking { getCityTemperatureAsync(cityName, BuildConfig.API_KEY) } doReturn GlobalScope.async {
                Response.error<WeatherResponse>(404, null)
            }
        }

        weatherRemoteDataSource = WeatherRemoteDataSourceImpl(weatherService, mainCoroutineRule.coroutineContext)



        val result = weatherRemoteDataSource.getCityTemperature(cityName)




    }


}