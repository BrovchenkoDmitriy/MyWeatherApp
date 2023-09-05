package com.example.myweatherapp.data

import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.DailyWeatherEntity
import com.example.myweatherapp.domain.HourlyWeatherEntity

sealed class RequestStatus
class Success(
    val currentWeatherEntity: CurrentWeatherEntity,
    val dailyWeatherEntity: List<DailyWeatherEntity>,
    val hourlyWeatherEntity: List<HourlyWeatherEntity>
) : RequestStatus()
class Error(val error: String) : RequestStatus()
