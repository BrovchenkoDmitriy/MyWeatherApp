package com.example.testretrofitapp.data

import android.app.Application
import com.example.testretrofitapp.data.network.WeatherApi
import com.example.testretrofitapp.data.network.WeatherDto
import com.example.testretrofitapp.domain.WeatherEntity
import com.example.testretrofitapp.domain.WeatherForecastRepository

class WeatherForecastRepositoryImpl(application: Application):WeatherForecastRepository
{

    private val mapper = WeatherListMapper()
    private val apiService = WeatherApi.retrofitService
    private lateinit var weatherDto: WeatherDto
    //private val weatherJson = WeatherApi.retrofitService.getWeather()

     override fun getWeatherEntity(): WeatherEntity {
        //val a = mapper.mapDtoToEntity(weatherDto)
        return mapper.mapDtoToEntity(weatherDto)

    }

    override suspend fun loadData() {
       weatherDto = apiService.getWeather()
    }
}