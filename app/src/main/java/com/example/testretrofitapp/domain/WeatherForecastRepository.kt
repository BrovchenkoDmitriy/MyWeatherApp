package com.example.testretrofitapp.domain

interface WeatherForecastRepository {
    suspend fun getCurrentWeather(): CurrentWeatherEntity
    suspend fun getWeekWeather():List<DailyWeatherEntity>
    suspend fun getHourlyWeather():List<HourlyWeatherEntity>
    suspend fun loadData(lat: Double,
                         lon: Double,
                         exclude: String,
                         appid: String,
                         units: String,
                         lang: String)
}