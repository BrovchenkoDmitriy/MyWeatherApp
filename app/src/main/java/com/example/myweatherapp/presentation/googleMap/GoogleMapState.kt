package com.example.myweatherapp.presentation.googleMap

import com.example.myweatherapp.domain.CurrentWeatherEntity

sealed class GoogleMapState
class Success(
    val currentWeatherEntity: CurrentWeatherEntity
) : GoogleMapState()

object Loading : GoogleMapState()
class Error(val error: String) : GoogleMapState()
