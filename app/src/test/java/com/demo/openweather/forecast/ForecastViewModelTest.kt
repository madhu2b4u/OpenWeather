package com.demo.openweather.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.demo.openweather.LiveDataTestUtil
import com.demo.openweather.MainCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.demo.openweather.common.Status
import com.demo.openweather.common.Result
import com.demo.openweather.forecast.data.model.*
import com.demo.openweather.forecast.data.model.Clouds
import com.demo.openweather.forecast.data.model.Main
import com.demo.openweather.forecast.data.model.Sys
import com.demo.openweather.forecast.data.model.Weather
import com.demo.openweather.forecast.data.model.Wind
import com.demo.openweather.forecast.domain.ForecastUseCase
import com.demo.openweather.forecast.presentation.viewmodel.ForecastViewModel

class ForecastViewModelTest{

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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

    lateinit var useCase: ForecastUseCase

    lateinit var viewModel: ForecastViewModel

    @Before
    fun init(){
        useCase = mock ()
    }

    @Test
    fun testForecastRequest() = mainCoroutineRule.runBlockingTest {

        useCase = mock {
            onBlocking { getForecast(latitude, longitude) } doReturn object : LiveData<Result<ForecastResponse>>() {
                init {
                    value = Result.loading()
                }
            }
        }

        viewModel = ForecastViewModel(useCase)

        viewModel.fetchForecast(latitude, longitude)

        val result = viewModel.forecastResult

        result.observeForever {}
        kotlinx.coroutines.delay(2000)
        assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)

    }


    @Test
    fun testForecastSuccessData() = mainCoroutineRule.runBlockingTest {

        useCase = mock {
            onBlocking { getForecast(latitude, longitude) } doReturn object : LiveData<Result<ForecastResponse>>() {
                init {
                    value = Result.success(forecastResponse)
                }
            }
        }

        viewModel = ForecastViewModel(useCase)

        viewModel.fetchForecast(latitude, longitude)

        val result = viewModel.forecastResult

        result.observeForever {}

        kotlinx.coroutines.delay(2000)

        assert(
            LiveDataTestUtil.getValue(result).status == Status.SUCCESS &&
                LiveDataTestUtil.getValue(result).data == forecastResponse)


    }


    @Test
    fun testForecastErrorData() = mainCoroutineRule.runBlockingTest {

        useCase = mock {
            onBlocking {getForecast(latitude, longitude)} doReturn object : LiveData<Result<ForecastResponse>>() {
                init {
                    value = Result.error("error")
                }
            }
        }

        viewModel = ForecastViewModel(useCase)

        viewModel.fetchForecast(latitude, longitude)

        val result = viewModel.forecastResult

        result.observeForever {}


        kotlinx.coroutines.delay(2000)


        assert(
            LiveDataTestUtil.getValue(result).status == Status.ERROR &&
                LiveDataTestUtil.getValue(result).message == "error")

    }

}
