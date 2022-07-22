package com.example.testretrofitapp.data

import android.app.Application
import com.example.testretrofitapp.data.database.AppDataBase
import com.example.testretrofitapp.data.network.WeatherApi
import com.example.testretrofitapp.domain.CurrentWeatherEntity
import com.example.testretrofitapp.domain.DailyWeatherEntity
import com.example.testretrofitapp.domain.WeatherForecastRepository

class WeatherForecastRepositoryImpl(application: Application) : WeatherForecastRepository {
    private val db = AppDataBase.getInstance(application)
    private val currentWeatherDao = db.currentWeatherDao()
    private val dailyWeatherDao = db.dailyWeatherDao()
    private val mapper = WeatherListMapper()
    private val apiService = WeatherApi.retrofitService

    override fun getCurrentWeather(): CurrentWeatherEntity {
        val currentWeatherDbModel = currentWeatherDao.getWeatherDbModel()
        return mapper.mapCurrentDbModelToCurrentEntity(currentWeatherDbModel)
    }

    override fun getWeekWeather(): List<DailyWeatherEntity> {
        val listOfDailyWeatherDbModel = dailyWeatherDao.getWeatherWeek()
        return mapper.mapDailyDbModelListToDailyEntityList(listOfDailyWeatherDbModel)
    }

    override suspend fun loadData() {
        try {
            val weatherDto = apiService.getWeather()
            val currentWeather = mapper.mapCurrentDtoToCurrentDbModel(weatherDto.currentDto)
            val weekWeather = mapper.mapDailyDtoListToDailyDaoList(weatherDto.dailyDto)

            currentWeatherDao.clearCurrentWeather()
            currentWeatherDao.insertCurrentWeather(currentWeather)

            dailyWeatherDao.clearWeekWeather()
            dailyWeatherDao.insertWeatherWeek(weekWeather)

        } catch (e: Exception) {

        }
    }
}