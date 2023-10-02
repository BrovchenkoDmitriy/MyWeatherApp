package com.example.myweatherapp.domain

sealed class ResultOfResponse
class ResponseSuccess(
    val currentWeatherEntity: CurrentWeatherEntity,
    val dailyWeatherEntity: List<DailyWeatherEntity>,
    val hourlyWeatherEntity: List<HourlyWeatherEntity>
) : ResultOfResponse()

class ResponseError(val error: String) : ResultOfResponse()
