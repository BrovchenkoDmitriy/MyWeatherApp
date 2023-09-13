package com.example.myweatherapp.domain

data class CurrentWeatherEntity(
    val id:Int = 0,
    val dt: String = "",
    val temp: String = "",
    val feelsLike: String = "",
    val pressure: String = "",
    val humidity: String = "",
    val windSpeed: String = "",
    val windGust: String? = "",
    val windDeg: String = "",
    val description: String = "",
    val icon: String ="",
    val lat: Double=0.0,
    val lon: Double=0.0
)

data class DailyWeatherEntity(
    val id:Int = 0,
    val dt: String ="",
    val tempDay: String ="",
    val tempNight: String ="",
    val feelsLikeDay: String ="",
    val feelsLikeNight: String ="",
    val pressure: String ="",
    val humidity: String ="",
    val windSpeed: String ="",
    val windGust: String ="",
    val windDeg: String ="",
    val description: String ="",
    val icon: String ="",
    val pop: Int = 0
)

data class HourlyWeatherEntity(
    val id:Int = 0,
    val dt: String = "",
    val temp: String = "",
    val feelsLike: String = "",
    val pressure: String = "",
    val humidity: String = "",
    val windSpeed: String = "",
    val windGust: String = "",
    val windDeg: String = "",
    val description: String = "",
    val icon: String = "",
    val pop: String = ""
)