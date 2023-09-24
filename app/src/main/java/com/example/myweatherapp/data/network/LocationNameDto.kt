package com.example.myweatherapp.data.network

import com.squareup.moshi.Json

data class LocationNameDto(
    val name: String,
    @Json(name = "display_name")
    val displayName: String,
    val address: Address
)

data class Address(
    val town: String = "",
    val city: String = "",
    val county: String,
    val state: String,
    val region: String,
    val country: String,
)