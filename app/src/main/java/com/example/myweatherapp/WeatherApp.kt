package com.example.myweatherapp

import android.app.Application
import com.example.myweatherapp.di.DaggerApplicationComponent


class WeatherApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    companion object {
        const val EXCLUDE = "minutely,alerts"
        const val UNITS = "metric"
        const val LANG = "ru"
    }
}