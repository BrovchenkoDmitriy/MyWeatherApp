package com.example.testretrofitapp.domain

class GeHourlyWeatherUseCase(private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke() = weatherForecastRepository.getHourlyWeather()
}