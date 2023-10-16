package com.example.myweatherapp.presentation.currentWeather

sealed class CurrentWeatherState
object Success : CurrentWeatherState()

object Loading : CurrentWeatherState()
class Error(val error: String) : CurrentWeatherState()
