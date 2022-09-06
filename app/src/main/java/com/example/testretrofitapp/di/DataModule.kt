package com.example.testretrofitapp.di

import android.app.Application
import com.example.testretrofitapp.data.WeatherForecastRepositoryImpl
import com.example.testretrofitapp.data.database.AppDataBase
import com.example.testretrofitapp.data.database.CurrentWeatherDao
import com.example.testretrofitapp.data.database.DailyWeatherDao
import com.example.testretrofitapp.data.database.HourlyWeatherDao
import com.example.testretrofitapp.data.network.OpenWeatherAPi
import com.example.testretrofitapp.data.network.WeatherApi
import com.example.testretrofitapp.domain.WeatherForecastRepository
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