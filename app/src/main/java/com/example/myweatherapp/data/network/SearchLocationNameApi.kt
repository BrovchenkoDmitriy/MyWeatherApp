package com.example.myweatherapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import javax.inject.Inject

interface SearchLocationNameApi {

    @GET("reverse")
    suspend fun getLocationName(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("zoom") zoom: String,
        @Query("format") format:String
    ):LocationNameDto
}

class SearchLocationNameApiImpl @Inject constructor(
    private val searchLocationNameApi: SearchLocationNameApi
) : SearchLocationNameApi {
    override suspend fun getLocationName(
         lat: Double,
         lon: Double,
        zoom: String,
        format:String
    ): LocationNameDto {
        return searchLocationNameApi.getLocationName(lat, lon, zoom, format)
    }
}