package com.example.testretrofitapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DailyWeatherDao{
    @Query("SELECT * FROM daily_weather")
    fun getWeatherWeek():List<DailyWeatherDbModel>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherWeek(weatherWeek: List<DailyWeatherDbModel>)

    @Query("DELETE FROM daily_weather")
    suspend fun clearWeekWeather()
}
