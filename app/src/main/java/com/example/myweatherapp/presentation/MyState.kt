package com.example.myweatherapp.presentation

import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.DailyWeatherEntity
import com.example.myweatherapp.domain.HourlyWeatherEntity

sealed class MyState
class Success(
    val currentWeatherEntity: CurrentWeatherEntity,
    val dailyWeatherEntity: List<DailyWeatherEntity>,
    val hourlyWeatherEntity: List<HourlyWeatherEntity>
) : MyState()

object Loading : MyState()
class Error(val error: String) : MyState()
