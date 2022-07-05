package com.example.testretrofitapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
//https://api.openweathermap.org/data/2.5/
// onecall?lat=50.2997427&lon=127.5023826&exclude=minutely,hourly,alerts&appid=2e566c90702a14d162799e5be40e0a12&units=metric&lang=ru
// https://api.openweathermap.org/data/2.5/
// onecall?lat=50.2997427&lon=127.5023826&exclude=minutely,alerts&appid=2e566c90702a14d162799e5be40e0a12&units=metric&lang=ru
//   weather?q=Благовещенск&appid=2e566c90702a14d162799e5be40e0a12&units=metric&lang=ru"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object WeatherApi {
    val retrofitService: OpenWeatherAPi by lazy {
        retrofit.create(OpenWeatherAPi::class.java)
    }
}