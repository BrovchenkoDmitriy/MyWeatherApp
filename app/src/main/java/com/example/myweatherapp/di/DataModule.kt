package com.example.myweatherapp.di

import android.app.Application
import com.example.myweatherapp.data.database.AppDataBase
import com.example.myweatherapp.data.database.CurrentWeatherDao
import com.example.myweatherapp.data.database.DailyWeatherDao
import com.example.myweatherapp.data.database.HourlyWeatherDao
import com.example.myweatherapp.data.network.OpenWeatherAPi
import com.example.myweatherapp.data.network.SearchCityApi
import com.example.myweatherapp.data.network.SearchLocationNameApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

//    @Provides
//    fun provideUrl(): String = "http://api.openweathermap.org/data/2.5/"

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

//    @Provides
//    @Singleton
//    fun provideRetrofit1(moshi: Moshi): Retrofit =
//        Retrofit.Builder()
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .baseUrl("http://api.openweathermap.org/data/2.5/")
//            .build()

//    @Provides
//    @Singleton
//    fun provideRetrofit2(moshi: Moshi, url: String): Retrofit =
//        Retrofit.Builder()
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .baseUrl("https://api.mapbox.com/search/searchbox/v1/")
//            .build()

    @Provides
    @Singleton
    fun provideApiService(moshi: Moshi): OpenWeatherAPi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .build().create(OpenWeatherAPi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchCityApiService(moshi:Moshi):SearchCityApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/")
            .build().create(SearchCityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchLocationNameService(moshi: Moshi):SearchLocationNameApi{
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://nominatim.openstreetmap.org/")
            .build().create((SearchLocationNameApi::class.java))
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