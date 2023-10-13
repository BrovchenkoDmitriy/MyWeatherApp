package com.example.myweatherapp.presentation.currentWeather

import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.DailyWeatherEntity
import com.example.myweatherapp.domain.HourlyWeatherEntity

sealed class CurrentWeatherState
class Success(
    val currentWeatherEntity: CurrentWeatherEntity,
    val hourlyWeatherEntity: List<HourlyWeatherEntity>
) : CurrentWeatherState()

object Loading : CurrentWeatherState()
class Error(val error: String) : CurrentWeatherState()
