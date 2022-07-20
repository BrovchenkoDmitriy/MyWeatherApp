package com.example.testretrofitapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface WeatherForecastDao {

    @Query("SELECT * FROM weather_forecast")
    fun getWeatherDbModel(): WeatherDBModel
}