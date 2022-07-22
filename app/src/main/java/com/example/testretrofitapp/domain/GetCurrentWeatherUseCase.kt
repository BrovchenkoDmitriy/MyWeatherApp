package com.example.testretrofitapp.domain

class GetCurrentWeatherUseCase (private val weatherForecastRepository: WeatherForecastRepository) {
    operator fun invoke() = weatherForecastRepository.getCurrentWeather()
}