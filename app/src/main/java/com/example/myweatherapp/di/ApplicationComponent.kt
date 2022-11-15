package com.example.myweatherapp.di

import android.app.Application
import com.example.myweatherapp.presentation.currentWeather.OverviewFragment
import com.example.myweatherapp.presentation.googleMap.MapFragment
import com.example.myweatherapp.presentation.weekForecast.WeekForecastFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(fragment: OverviewFragment)
    fun inject(fragment:WeekForecastFragment)
    fun inject(fragment:MapFragment)

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance application: Application
        ):ApplicationComponent
    }
}