package com.demo.openweather.weather.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.openweather.common.Result
import com.demo.openweather.weather.data.model.WeatherResponse
import com.demo.openweather.weather.domain.WeatherFragmentUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject


class WeatherViewModel @Inject constructor(
    private val mWeatherFragmentUseCase: WeatherFragmentUseCase) : ViewModel() {

    val weatherResult = MediatorLiveData<Result<WeatherResponse>>()


    fun fetchWeather(city :String) {
        viewModelScope.launch {
            weatherResult.addSource(mWeatherFragmentUseCase.getTemperature(city)) {
                weatherResult.value = it
            }
        }
    }


}