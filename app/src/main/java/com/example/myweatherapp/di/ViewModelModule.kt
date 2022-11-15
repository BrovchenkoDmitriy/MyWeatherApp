package com.example.myweatherapp.di

import androidx.lifecycle.ViewModel
import com.example.myweatherapp.presentation.currentWeather.OverViewModel
import com.example.myweatherapp.presentation.googleMap.MapViewModel
import com.example.myweatherapp.presentation.weekForecast.WeekForecastViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(OverViewModel::class)
    fun bindOverViewModel(viewModel: OverViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeekForecastViewModel::class)
    fun bindWeekForecastViewModel(viewModel: WeekForecastViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    fun bindMapViewModel(viewModel: MapViewModel):ViewModel
}