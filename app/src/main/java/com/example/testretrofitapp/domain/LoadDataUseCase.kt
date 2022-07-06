package com.example.testretrofitapp.domain

class LoadDataUseCase(private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke() = weatherForecastRepository.loadData()
}