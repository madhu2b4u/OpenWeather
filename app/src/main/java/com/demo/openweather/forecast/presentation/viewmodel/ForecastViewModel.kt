package com.demo.openweather.forecast.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.openweather.common.Result
import com.demo.openweather.forecast.data.model.ForecastResponse
import com.demo.openweather.forecast.domain.ForecastUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject


class ForecastViewModel @Inject constructor(
    private val mForecastUseCase: ForecastUseCase
) : ViewModel() {

    val forecastResult = MediatorLiveData<Result<ForecastResponse>>()

    fun fetchForecast(latitude :String, longitude :String) {
        viewModelScope.launch {
            forecastResult.addSource(mForecastUseCase.getForecast(latitude,longitude)) {
                forecastResult.value = it
            }
        }
    }



}