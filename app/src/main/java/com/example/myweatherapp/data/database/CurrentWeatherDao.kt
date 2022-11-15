package com.example.myweatherapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM current_weather")
    suspend fun getWeatherDbModel(): CurrentWeatherDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeatherDbModel: CurrentWeatherDbModel)

    @Query("DELETE FROM current_weather")
    suspend fun clearCurrentWeather()
}

