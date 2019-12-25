package com.demo.openweather.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.demo.openweather.LiveDataTestUtil
import com.demo.openweather.MainCoroutineRule
import com.demo.openweather.common.Result
import com.demo.openweather.common.Status
import com.demo.openweather.forecast.data.model.*
import com.demo.openweather.forecast.data.repository.ForecastRepository
import com.demo.openweather.forecast.domain.ForecastUseCase
import com.demo.openweather.forecast.domain.ForecastUseCaseImpl
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ForecastUseCaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var forecastUseCase: ForecastUseCase

    private lateinit var repositary: ForecastRepository


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





    @Test
    fun testLoadingData()=mainCoroutineRule.runBlockingTest{
            repositary = mock {
                onBlocking { getForecast(latitude,longitude) } doReturn object : LiveData<Result<ForecastResponse>>() {
                    init {
                        value = Result.loading()
                    }
                }
            }
            forecastUseCase = ForecastUseCaseImpl(repositary)

            val result = forecastUseCase.getForecast(latitude, longitude)

            result.observeForever {  }

            assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)

        }


    @Test
    fun testSuccessData()=mainCoroutineRule.runBlockingTest{
        repositary = mock {
            onBlocking { getForecast(latitude, longitude) } doReturn object : LiveData<Result<ForecastResponse>>() {
                init {
                    value = Result.success(forecastResponse)
                }
            }
        }
        forecastUseCase = ForecastUseCaseImpl(repositary)

        val result = forecastUseCase.getForecast(latitude, longitude)

        result.observeForever {  }

        assert(LiveDataTestUtil.getValue(result).status == Status.SUCCESS && LiveDataTestUtil.getValue(result).data == forecastResponse)

    }

    @Test
    fun testErrorData()=mainCoroutineRule.runBlockingTest{
        repositary = mock {
            onBlocking { getForecast(latitude, longitude) } doReturn object : LiveData<Result<ForecastResponse>>() {
                init {
                    value = Result.error("error")
                }
            }
        }
        forecastUseCase = ForecastUseCaseImpl(repositary)

        val result = forecastUseCase.getForecast(latitude, longitude)

        result.observeForever {  }

        assert(LiveDataTestUtil.getValue(result).status == Status.ERROR && LiveDataTestUtil.getValue(result).message == "error")

    }


}
