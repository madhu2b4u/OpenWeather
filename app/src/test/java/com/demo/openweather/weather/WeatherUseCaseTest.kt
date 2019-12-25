package com.demo.openweather.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.demo.openweather.LiveDataTestUtil
import com.demo.openweather.MainCoroutineRule
import com.demo.openweather.weather.data.model.*
import com.demo.openweather.weather.data.repository.WeatherRepository
import com.demo.openweather.weather.domain.WeatherFragmentUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import com.demo.openweather.common.Result
import com.demo.openweather.common.Status
import com.demo.openweather.weather.domain.WeatherFragmentUseCaseImpl

@ExperimentalCoroutinesApi
class WeatherUseCaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var getArticlesUseCase: WeatherFragmentUseCase

    private lateinit var articleRepository: WeatherRepository


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





    @Test
    fun testLoadingData()=mainCoroutineRule.runBlockingTest{
            articleRepository = mock {
                onBlocking { getCityTemperature(cityName) } doReturn object : LiveData<Result<WeatherResponse>>() {
                    init {
                        value = Result.loading()
                    }
                }
            }
            getArticlesUseCase = WeatherFragmentUseCaseImpl(articleRepository)

            val result = getArticlesUseCase.getTemperature(cityName)

            result.observeForever {  }

            assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)

        }


    @Test
    fun testSuccessData()=mainCoroutineRule.runBlockingTest{
        articleRepository = mock {
            onBlocking { getCityTemperature(cityName) } doReturn object : LiveData<Result<WeatherResponse>>() {
                init {
                    value = Result.success(weatherResponse)
                }
            }
        }
        getArticlesUseCase = WeatherFragmentUseCaseImpl(articleRepository)

        val result = getArticlesUseCase.getTemperature(cityName)

        result.observeForever {  }

        assert(LiveDataTestUtil.getValue(result).status == Status.SUCCESS && LiveDataTestUtil.getValue(result).data == weatherResponse)

    }

    @Test
    fun testWeatherErrorData()=mainCoroutineRule.runBlockingTest{
        articleRepository = mock {
            onBlocking { getCityTemperature(cityName) } doReturn object : LiveData<Result<WeatherResponse>>() {
                init {
                    value = Result.error("error")
                }
            }
        }
        getArticlesUseCase = WeatherFragmentUseCaseImpl(articleRepository)

        val result = getArticlesUseCase.getTemperature(cityName)

        result.observeForever {  }

        assert(LiveDataTestUtil.getValue(result).status == Status.ERROR && LiveDataTestUtil.getValue(result).message == "error")

    }


}
