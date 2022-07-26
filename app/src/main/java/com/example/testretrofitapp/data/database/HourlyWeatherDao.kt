package com.example.testretrofitapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface HourlyWeatherDao{
    @Query("SELECT * FROM hourly_weather")
    fun getHourlyWeather():List<HourlyWeatherDbModel>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(hourlyWeatherList: List<HourlyWeatherDbModel>)

    @Query("DELETE FROM hourly_weather")
    suspend fun clearHourlyWeather()
}
