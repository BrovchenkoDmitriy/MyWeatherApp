package com.example.testretrofitapp.domain

import javax.inject.Inject

class GetWeekWeatherUseCase @Inject constructor(private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke() = weatherForecastRepository.getWeekWeather()
}