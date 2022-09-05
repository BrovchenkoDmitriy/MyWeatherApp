package com.example.testretrofitapp

import android.app.Application
import com.example.testretrofitapp.di.DaggerApplicationComponent


class WeatherApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}