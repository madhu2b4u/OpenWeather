package com.demo.openweather.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.openweather.LiveDataTestUtil
import com.demo.openweather.MainCoroutineRule
import com.demo.openweather.common.Status
import com.demo.openweather.forecast.data.model.*
import com.demo.openweather.forecast.data.model.Clouds
import com.demo.openweather.forecast.data.model.Main
import com.demo.openweather.forecast.data.model.Sys
import com.demo.openweather.forecast.data.model.Weather
import com.demo.openweather.forecast.data.model.Wind
import com.demo.openweather.forecast.data.remote.services.ForecastService
import com.demo.openweather.forecast.data.remote.source.ForecastRemoteDataSource
import com.demo.openweather.forecast.data.repository.ForecastRepository
import com.demo.openweather.forecast.data.repository.ForecastRepositoryImpl
import com.demo.openweather.weather.data.model.*
import com.demo.openweather.weather.data.remote.services.WeatherService
import com.demo.openweather.weather.data.remote.source.WeatherRemoteDataSource
import com.demo.openweather.weather.data.repository.WeatherRepository
import com.demo.openweather.weather.data.repository.WeatherRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ForecastRepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    lateinit var repository: ForecastRepository

    @Mock
    lateinit var remoteDataSource: ForecastRemoteDataSource


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
        MockitoAnnotations.initMocks(this)
        repository = ForecastRepositoryImpl(remoteDataSource)
    }

    @Test
    fun testForecastFromAPI() = mainCoroutineRule.runBlockingTest {

        Mockito.`when`(remoteDataSource.getForecast(latitude,longitude)).thenReturn(forecastResponse)
        val result = repository.getForecast(latitude,longitude)
        assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)
        assert(LiveDataTestUtil.getValue(result).status == Status.SUCCESS)
        assert(LiveDataTestUtil.getValue(result).data == forecastResponse)


    }


    @Test(expected = Exception::class)
    fun testForecastThrowException() = mainCoroutineRule.runBlockingTest {
        Mockito.doThrow(Exception("error")).`when`(remoteDataSource.getForecast(latitude,longitude))
        repository.getForecast(latitude,longitude)


    }


}
