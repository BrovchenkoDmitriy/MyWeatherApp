package com.example.testretrofitapp.domain

class GetWeatherEntityUseCase (private val weatherForecastRepository: WeatherForecastRepository) {
    operator fun invoke() = weatherForecastRepository.getWeatherEntity()
}