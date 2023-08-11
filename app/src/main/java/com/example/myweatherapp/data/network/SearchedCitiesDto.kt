package com.example.myweatherapp.data.network

import com.squareup.moshi.Json

data class SearchedCitiesDto (
    val suggestions: List<Suggestion>
)

data class Suggestion (
    val value: String,

    @Json(name = "unrestricted_value")
    val unrestrictedValue: String,

    val data: Data
)

data class Data (
    @Json(name = "postal_code")
    val postalCode: String? = null,

    val country: String?,

    @Json(name = "country_iso_code")
    val countryISOCode: String?,

    @Json(name = "federal_district")
    val federalDistrict: String?,

    @Json(name = "region_iso_code")
    val regionISOCode: String?,

    @Json(name = "region_with_type")
    val regionWithType: String?,

    @Json(name = "region_type")
    val regionType: String?,

    @Json(name = "region_type_full")
    val regionTypeFull: String?,

    val region: String?,


    @Json(name = "city_with_type")
    val cityWithType: String?,

    val city: String?,

    @Json(name = "city_area")
    val cityArea: String? = null,

    @Json(name = "street_type_full")
    val streetTypeFull: String? = null,

    val street: String? = null,


    @Json(name = "geo_lat")
    val geoLat: String?,

    @Json(name = "geo_lon")
    val geoLon: String?,



)