package com.example.myweatherapp.domain

import javax.inject.Inject

class LoadDataUseCase @Inject constructor(private val weatherForecastRepository: WeatherForecastRepository) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        units: String,
        lang: String
    ) = weatherForecastRepository.loadData(
        lat,
        lon,
        exclude,
        appid,
        units,
        lang
    )
}