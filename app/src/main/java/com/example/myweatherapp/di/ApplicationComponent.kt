package com.example.myweatherapp.di

import android.app.Application
import com.example.myweatherapp.presentation.currentWeather.MainWeatherFragment
import com.example.myweatherapp.presentation.googleMap.MapFragment
import com.example.myweatherapp.presentation.weekForecast.WeekForecastFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class, DomainModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(fragment: MainWeatherFragment)
    fun inject(fragment:WeekForecastFragment)
    fun inject(fragment:MapFragment)

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance application: Application
        ):ApplicationComponent
    }
}