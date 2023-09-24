package com.example.myweatherapp.data.network

import com.squareup.moshi.Json

data  class LocationNameDto(
    val name: String,
    @Json(name = "display_name")
    val displayName: String,
)