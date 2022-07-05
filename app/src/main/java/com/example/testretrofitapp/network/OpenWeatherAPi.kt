package com.example.testretrofitapp.network

import retrofit2.http.GET

private const val GET_VALUE=
    //"weather?q=Чигири&appid=2e566c90702a14d162799e5be40e0a12&units=metric&lang=ru"
    "onecall?lat=50.2997427&lon=127.5023826&exclude=minutely,alerts&appid=2e566c90702a14d162799e5be40e0a12&units=metric&lang=ru"


interface OpenWeatherAPi {
    @GET(GET_VALUE)
    suspend fun getWeather(): WeatherDto
}