package com.example.myweatherapp.presentation.weekForecast

import com.example.myweatherapp.domain.DailyWeatherEntity

sealed class WeekForecastState
class Success(
    val dailyWeatherEntity: List<DailyWeatherEntity>
) : WeekForecastState()

object Loading : WeekForecastState()
