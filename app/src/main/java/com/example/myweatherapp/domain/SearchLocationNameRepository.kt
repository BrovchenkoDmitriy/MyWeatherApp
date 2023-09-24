package com.example.myweatherapp.domain

interface SearchLocationNameRepository {
    suspend fun getLocationName(lat:Double, lon: Double): String
}