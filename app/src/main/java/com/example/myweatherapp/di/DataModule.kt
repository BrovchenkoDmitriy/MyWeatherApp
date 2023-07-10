package com.example.myweatherapp.di

import android.app.Application
import com.example.myweatherapp.data.database.AppDataBase
import com.example.myweatherapp.data.database.CurrentWeatherDao
import com.example.myweatherapp.data.database.DailyWeatherDao
import com.example.myweatherapp.data.database.HourlyWeatherDao
import com.example.myweatherapp.data.network.OpenWeatherAPi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideUrl(): String = "http://api.openweathermap.org/data/2.5/"

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, url: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(url)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): OpenWeatherAPi {
        return retrofit.create(OpenWeatherAPi::class.java)
    }

    @Provides
    @ApplicationScope
    fun provideCurrentWeatherDao(application: Application): CurrentWeatherDao {
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
}