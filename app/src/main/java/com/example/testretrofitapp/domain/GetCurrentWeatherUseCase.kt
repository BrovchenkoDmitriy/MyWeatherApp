package com.example.testretrofitapp.domain

class GetCurrentWeatherUseCase (private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke() = weatherForecastRepository.getCurrentWeather()
}