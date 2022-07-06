package com.example.testretrofitapp.domain

interface WeatherForecastRepository {
    fun getWeatherEntity(): WeatherEntity
    suspend fun loadData()
}