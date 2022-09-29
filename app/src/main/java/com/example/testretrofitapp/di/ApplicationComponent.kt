package com.example.testretrofitapp.di

import android.app.Application
import com.example.testretrofitapp.presentation.currentWeather.OverviewFragment
import com.example.testretrofitapp.presentation.googleMap.MapFragment
import com.example.testretrofitapp.presentation.weekForecast.WeekForecastFragment
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