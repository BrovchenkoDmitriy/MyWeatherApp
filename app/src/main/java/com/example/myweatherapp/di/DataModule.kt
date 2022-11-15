package com.example.myweatherapp.di

import android.app.Application
import com.example.myweatherapp.data.WeatherForecastRepositoryImpl
import com.example.myweatherapp.data.database.AppDataBase
import com.example.myweatherapp.data.database.CurrentWeatherDao
import com.example.myweatherapp.data.database.DailyWeatherDao
import com.example.myweatherapp.data.database.HourlyWeatherDao
import com.example.myweatherapp.data.network.OpenWeatherAPi
import com.example.myweatherapp.data.network.WeatherApi
import com.example.myweatherapp.domain.WeatherForecastRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindWeatherForecastRepository(
        impl: WeatherForecastRepositoryImpl
    ): WeatherForecastRepository

    companion object{
        @Provides
        @ApplicationScope
        fun provideCurrentWeatherDao(application: Application):CurrentWeatherDao{
            return AppDataBase.getInstance(application).currentWeatherDao()
        }

        @Provides
        @ApplicationScope
        fun provideDailyWeatherDao(application: Application): DailyWeatherDao {
            return AppDataBase.getInstance(application).dailyWeatherDao()
        }

        @Provides
        @ApplicationScope
        fun provideHourlyWeatherDao(application: Application): HourlyWeatherDao {
            return AppDataBase.getInstance(application).hourlyWeatherDao()
        }

        @Provides
        @ApplicationScope
        fun provideOpenWeatherApi(): OpenWeatherAPi {
            return WeatherApi.retrofitService
        }

    }
}