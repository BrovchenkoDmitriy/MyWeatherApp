package com.example.testretrofitapp.domain

import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor (private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke() = weatherForecastRepository.getCurrentWeather()
}