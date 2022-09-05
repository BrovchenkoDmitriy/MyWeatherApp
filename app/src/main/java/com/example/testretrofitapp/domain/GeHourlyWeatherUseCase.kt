package com.example.testretrofitapp.domain

import javax.inject.Inject

class GeHourlyWeatherUseCase @Inject constructor(private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke() = weatherForecastRepository.getHourlyWeather()
}