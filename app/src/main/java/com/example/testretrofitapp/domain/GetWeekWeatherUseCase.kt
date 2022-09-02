package com.example.testretrofitapp.domain

class GetWeekWeatherUseCase(private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke() = weatherForecastRepository.getWeekWeather()
}