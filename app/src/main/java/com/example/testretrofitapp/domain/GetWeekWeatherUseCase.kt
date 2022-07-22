package com.example.testretrofitapp.domain

class GetWeekWeatherUseCase(private val weatherForecastRepository: WeatherForecastRepository) {
    operator fun invoke() = weatherForecastRepository.getWeekWeather()
}