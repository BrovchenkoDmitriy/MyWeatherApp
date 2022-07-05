//package com.example.testretrofitapp.domain
//
//data class WeatherEntity(
//    val lat: String,
//    val lon: String,
//    val timezone: String,
//    val timezoneOffset: String,
//    val currentEntity: CurrentWeatherEntity,
//    val hourlyEntity: List<HourlyWeatherEntity>,
//    val dailyEntity: List<DailyWeatherEntity>
//)
//
//data class CurrentWeatherEntity(
//    val dt: String,
//    val temp: String,
//    val feelsLike: String,
//    val pressure: String,
//    val humidity: String,
//    val weather: WeatherTitleEntity
//)
//
//data class DailyWeatherEntity(
//    val dt: String,
//    val temp: DailyTempEntity,
//    val feelsLike: DailyTempEntity,
//    val pressure: String,
//    val humidity: String,
//    val windSpeed: String,
//    val windDeg: String,
//    val WindGust: String,
//    val weather: WeatherTitleEntity,
//    val pop: String
//)
//
//data class HourlyWeatherEntity(
//    val dt: String,
//    val temp: String,
//    val feelsLike: String,
//    val pressure: String,
//    val humidity: String,
//    val windSpeed: String,
//    val windDeg: String,
//    val WindGust: String,
//    val weather: WeatherTitleEntity,
//    val pop: String
//)
//
//data class DailyTempEntity(
//    val day: String,
//    val night: String
//)
//
//data class WeatherTitleEntity(
//    val id: String,
//    val main: String,
//    val description: String,
//    val icon: String
//)