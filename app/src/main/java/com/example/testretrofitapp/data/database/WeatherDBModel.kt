package com.example.testretrofitapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "current_weather")
data class CurrentWeatherDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dt: String,
    val temp: String,
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windGust: String?,
    val windDeg: String,
    val description: String,
    val icon: String
)

@Entity(tableName = "daily_weather")
data class DailyWeatherDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dt: String,
    val tempDay: String,
    val tempNight: String,
    val feelsLikeDay: String,
    val feelsLikeNight: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windGust: String,
    val windDeg: String,
    val description: String,
    val icon: String,
    val pop: String
)

@Entity(tableName = "hourly_weather")
data class HourlyWeatherDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dt: String,
    val temp: String,
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val windGust: String,
    val windDeg: String,
    val description: String,
    val icon: String,
    val pop: String
)