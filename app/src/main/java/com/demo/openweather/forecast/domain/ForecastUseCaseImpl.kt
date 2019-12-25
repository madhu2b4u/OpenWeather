package com.demo.openweather.forecast.domain

import com.demo.openweather.forecast.data.repository.ForecastRepository
import javax.inject.Inject

class ForecastUseCaseImpl @Inject constructor(private val mForecastRepository: ForecastRepository) :
    ForecastUseCase {
    override suspend fun getForecast(
        latitude: String,
        longitude: String) = mForecastRepository.getForecast(latitude,longitude)

}
