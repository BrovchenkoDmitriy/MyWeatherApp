package com.example.myweatherapp.data.network

import com.squareup.moshi.Json

data class SearchedCitiesDto (
    val suggestions: List<Suggestion>,
    val attribution: String,
    val url: String
)

data class Suggestion (
    val name: String,

    @Json(name = "name_preferred")
    val namePreferred: String,

    @Json(name = "place_formatted")
    val placeFormatted: String,
)