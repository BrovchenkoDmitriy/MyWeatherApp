package com.example.myweatherapp.di

import com.example.myweatherapp.data.SearchCityAutocompleteRepositoryImpl
import com.example.myweatherapp.data.WeatherForecastRepositoryImpl
import com.example.myweatherapp.domain.SearchCityAutocompleteRepository
import com.example.myweatherapp.domain.WeatherForecastRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    @ApplicationScope
    fun bindWeatherForecastRepository(
        impl: WeatherForecastRepositoryImpl
    ): WeatherForecastRepository

    @Binds
    @ApplicationScope
    fun bindSearchCitiesAutocompleteRepository(
        impl: SearchCityAutocompleteRepositoryImpl
    ): SearchCityAutocompleteRepository
}