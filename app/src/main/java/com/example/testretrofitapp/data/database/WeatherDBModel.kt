package com.example.testretrofitapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "weather_forecast")
data class  WeatherDBModel (

    val lat: String,
    val lon: String,
    val timezone: String,
    val timezoneOffset: String,
    val currentDao: CurrentWeatherDao,
    val hourlyDao: List<HourlyWeatherDao>,
    val dailyDao: List<DailyWeatherDao>
)

data class CurrentWeatherDao(
    val dt: String,
    val temp: String,
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windGust: String?,
    val windDeg: String,
    val weather: WeatherTitleDao
)

data class DailyWeatherDao(
    val dt: String,
    val temp: DailyTempDao,
    val feelsLike: DailyTempDao,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windGust: String,
    val windDeg: String,
    val weather: WeatherTitleDao,
    val pop: String
)

data class HourlyWeatherDao(
    val dt: String,
    val temp: String,
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windGust: String,
    val windDeg: String,
    val weather: WeatherTitleDao,
    val pop: String
)

data class DailyTempDao(
    val day: String,
    val night: String
)

data class WeatherTitleDao(
    val id: String,
    val main: String,
    val description: String,
    val icon: String
)