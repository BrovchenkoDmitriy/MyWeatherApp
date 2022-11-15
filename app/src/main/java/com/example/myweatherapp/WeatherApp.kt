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
    }
}