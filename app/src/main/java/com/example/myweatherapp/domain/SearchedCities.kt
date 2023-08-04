package com.example.myweatherapp.domain

data class SearchedCities(
    val name: String,
    val namePreferred: String,
    val placeFormatted: String
)