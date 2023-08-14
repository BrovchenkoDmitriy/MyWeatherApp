package com.example.myweatherapp.domain

data class SearchedCities(
    val value: String,
    val unrestrictedValue: String,

    val postalCode: String? = null,
    val country: String,
    val countryISOCode: String,
    val federalDistrict: String,

    val regionISOCode: String,
    val regionWithType: String,
    val regionType: String,
    val regionTypeFull: String,
    val region: String,

    val cityWithType: String,
    val city: String,
    val cityArea: String? = null,

    val streetTypeFull: String? = null,
    val street: String? = null,

    val geoLat: Double,
    val geoLon: Double
)
