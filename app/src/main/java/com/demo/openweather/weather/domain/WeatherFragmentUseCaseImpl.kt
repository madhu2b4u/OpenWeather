package com.demo.openweather.weather.domain

import com.demo.openweather.weather.data.repository.WeatherRepository
import javax.inject.Inject

class WeatherFragmentUseCaseImpl @Inject constructor(private val mWeatherRepository: WeatherRepository) :
    WeatherFragmentUseCase {
    override suspend fun getTemperature(city :String) = mWeatherRepository.getCityTemperature(city)

}
