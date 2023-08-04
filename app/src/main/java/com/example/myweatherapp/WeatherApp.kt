package com.example.myweatherapp

import android.app.Application
import com.example.myweatherapp.di.DaggerApplicationComponent


class WeatherApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    companion object {
        const val EXCLUDE = "minutely,alerts"
        const val APPID= "2e566c90702a14d162799e5be40e0a12"
        const val UNITS = "metric"
        const val LANG = "ru"

        const val PLACE_TYPE = "city"
        const val SESSION_TOKEN = "0e4973c0-1e48-4f21-889b-8fd0d3406291"
        const val ACCESS_TOKEN = "pk.eyJ1IjoiYW5kcm9pZC1kZXYiLCJhIjoiY2xrdWdkYjd5MDhkcjNlcjAwandtZmdtMSJ9.zkuJuGg6loC2zhkpK5w0-Q"
    }
}