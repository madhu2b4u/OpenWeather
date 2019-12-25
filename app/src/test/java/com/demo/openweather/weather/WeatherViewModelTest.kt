package com.demo.openweather.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.demo.openweather.LiveDataTestUtil
import com.demo.openweather.MainCoroutineRule
import com.demo.openweather.weather.data.model.*
import com.demo.openweather.weather.domain.WeatherFragmentUseCase
import com.demo.openweather.weather.presentation.viewmodel.WeatherViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.demo.openweather.common.Status
import com.demo.openweather.common.Result

class WeatherViewModelTest{

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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

    lateinit var useCase: WeatherFragmentUseCase

    lateinit var viewModel: WeatherViewModel

    @Before
    fun init(){
        useCase = mock ()
    }

    @Test
    fun testWeatherRequest() = mainCoroutineRule.runBlockingTest {

        useCase = mock {
            onBlocking { getTemperature(cityName) } doReturn object : LiveData<Result<WeatherResponse>>() {
                init {
                    value = Result.loading()
                }
            }
        }

        viewModel = WeatherViewModel(useCase)

        viewModel.fetchWeather(cityName)

        val result = viewModel.weatherResult

        result.observeForever {}
        kotlinx.coroutines.delay(2000)
        assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)

    }


    @Test
    fun testWeatherSuccessData() = mainCoroutineRule.runBlockingTest {

        useCase = mock {
            onBlocking { getTemperature(cityName) } doReturn object : LiveData<Result<WeatherResponse>>() {
                init {
                    value = Result.success(weatherResponse)
                }
            }
        }

        viewModel = WeatherViewModel(useCase)

        viewModel.fetchWeather(cityName)

        val result = viewModel.weatherResult

        result.observeForever {}

        kotlinx.coroutines.delay(2000)

        assert(
            LiveDataTestUtil.getValue(result).status == Status.SUCCESS &&
                LiveDataTestUtil.getValue(result).data == weatherResponse)


    }


    @Test
    fun testWeatherFundErrorData() = mainCoroutineRule.runBlockingTest {

        useCase = mock {
            onBlocking {getTemperature(cityName)} doReturn object : LiveData<Result<WeatherResponse>>() {
                init {
                    value = Result.error("error")
                }
            }
        }

        viewModel = WeatherViewModel(useCase)

        viewModel.fetchWeather(cityName)

        val result = viewModel.weatherResult

        result.observeForever {}


        kotlinx.coroutines.delay(2000)


        assert(
            LiveDataTestUtil.getValue(result).status == Status.ERROR &&
                LiveDataTestUtil.getValue(result).message == "error")

    }

}
