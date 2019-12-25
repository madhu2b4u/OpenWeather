package com.demo.openweather.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.openweather.LiveDataTestUtil
import com.demo.openweather.MainCoroutineRule
import com.demo.openweather.common.Status
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
class WeatherRepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    lateinit var repository: WeatherRepository

    @Mock
    lateinit var remoteDataSource: WeatherRemoteDataSource

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
        MockitoAnnotations.initMocks(this)

        repository = WeatherRepositoryImpl(remoteDataSource)


    }

    @Test
    fun testGetWeatherFromAPI() = mainCoroutineRule.runBlockingTest {

        Mockito.`when`(remoteDataSource.getCityTemperature(cityName)).thenReturn(weatherResponse)


        val result = repository.getCityTemperature(cityName)


        assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)

        assert(LiveDataTestUtil.getValue(result).status == Status.SUCCESS)
        assert(LiveDataTestUtil.getValue(result).data == weatherResponse)


    }


    @Test(expected = Exception::class)
    fun testGetWeatherThrowException() = mainCoroutineRule.runBlockingTest {

        Mockito.doThrow(Exception("error")).`when`(remoteDataSource.getCityTemperature(cityName))

        repository.getCityTemperature(cityName)


    }


}
