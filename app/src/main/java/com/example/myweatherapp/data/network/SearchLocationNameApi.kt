package com.example.myweatherapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface SearchLocationNameApi {

    @GET("reverse")
    suspend fun getLocationName(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("zoom") zoom: String,
        @Query("format") format:String,
        @Query("accept-language") acceptLanguage: String
    ):LocationNameDto
}

class SearchLocationNameApiImpl @Inject constructor(
    private val searchLocationNameApi: SearchLocationNameApi
) : SearchLocationNameApi {
    override suspend fun getLocationName(
         lat: Double,
         lon: Double,
        zoom: String,
        format:String,
         acceptLanguage:String
    ): LocationNameDto {
        return searchLocationNameApi.getLocationName(lat, lon, zoom, format, acceptLanguage)
    }
}