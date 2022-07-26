package com.example.testretrofitapp.domain

interface WeatherForecastRepository {
    fun getCurrentWeather(): CurrentWeatherEntity
    fun getWeekWeather():List<DailyWeatherEntity>
    fun getHourlyWeather():List<HourlyWeatherEntity>
    suspend fun loadData()
}