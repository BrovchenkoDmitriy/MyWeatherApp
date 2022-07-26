package com.example.testretrofitapp.domain

class GeHourlyWeatherUseCase(private val weatherForecastRepository: WeatherForecastRepository) {
    operator fun invoke() = weatherForecastRepository.getHourlyWeather()
}