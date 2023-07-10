package com.example.myweatherapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface OpenWeatherAPi {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): WeatherDto
}

class WeatherApiImpl @Inject constructor(
    private val weatherAPi: OpenWeatherAPi
):OpenWeatherAPi{
    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        units: String,
        lang: String
    ): WeatherDto {
        return weatherAPi.getWeather(lat,lon,exclude,appid,units,lang)
    }
}