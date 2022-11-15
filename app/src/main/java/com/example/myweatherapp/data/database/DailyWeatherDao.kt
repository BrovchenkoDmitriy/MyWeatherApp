package com.example.myweatherapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DailyWeatherDao{
    @Query("SELECT * FROM daily_weather")
    suspend fun getWeatherWeek():List<DailyWeatherDbModel>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeekWeather(weatherWeek: List<DailyWeatherDbModel>)

    @Query("DELETE FROM daily_weather")
    suspend fun clearWeekWeather()
}
