package com.example.testretrofitapp.domain

interface WeatherForecastRepository {
    fun getCurrentWeather(): CurrentWeatherEntity
    fun getWeekWeather():List<DailyWeatherEntity>
    suspend fun loadData()
}