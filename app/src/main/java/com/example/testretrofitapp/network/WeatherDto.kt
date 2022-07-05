package com.example.testretrofitapp.network

import com.squareup.moshi.Json


data class WeatherDto(
    val lat: String,
    val lon: String,
    val timezone: String,
    @Json(name = "timezone_offset") val timezoneOffset: String,
    @Json(name = "current") val currentDto: CurrentWeatherDto,
   // @Json(name = "hourly") val hourlyDto: List<HourlyWeatherDto>,
    @Json(name = "daily") val dailyDto: List<DailyWeatherDto>
)

data class CurrentWeatherDto(
    val dt: String,
    val temp: String,
    @Json(name = "feels_like") val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val weather: List<WeatherTitleDto>
)

data class DailyWeatherDto(
    val dt: String,
    val temp: DailyTempDto,
    @Json(name = "feels_like") val feelsLike: DailyTempDto,
    val pressure: String,
    val humidity: String,
    @Json(name = "wind_speed") val windSpeed: String,
    @Json(name = "wind_deg") val windDeg: String,
    @Json(name = "wind_gust") val WindGust: String,
    val weather: List<WeatherTitleDto>,
    val pop: String
)

data class HourlyWeatherDto(
    val dt: String,
    val temp: String,
    @Json(name = "feels_like") val feelsLike: String,
    val pressure: String,
    val humidity: String,
    @Json(name = "wind_speed") val windSpeed: String,
    @Json(name = "wind_deg") val windDeg: String,
    @Json(name = "wind_gust") val WindGust: String,
    val weather: List<WeatherTitleDto>,
    val pop: String
)

data class DailyTempDto(
    val day: String,
    val night: String
)

data class WeatherTitleDto(
    val id: String,
    val main: String,
    val description: String,
    val icon: String
)

