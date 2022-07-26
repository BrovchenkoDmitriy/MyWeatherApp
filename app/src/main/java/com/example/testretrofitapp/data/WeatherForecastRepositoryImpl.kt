package com.example.testretrofitapp.data

import android.app.Application
import com.example.testretrofitapp.data.database.AppDataBase
import com.example.testretrofitapp.data.network.WeatherApi
import com.example.testretrofitapp.domain.CurrentWeatherEntity
import com.example.testretrofitapp.domain.DailyWeatherEntity
import com.example.testretrofitapp.domain.HourlyWeatherEntity
import com.example.testretrofitapp.domain.WeatherForecastRepository

class WeatherForecastRepositoryImpl(application: Application) : WeatherForecastRepository {
    private val db = AppDataBase.getInstance(application)
    private val currentWeatherDao = db.currentWeatherDao()
    private val dailyWeatherDao = db.dailyWeatherDao()
    private val hourlyWeatherDao = db.hourlyWeatherDao()
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

    override fun getHourlyWeather(): List<HourlyWeatherEntity> {
        val listOfHourlyWeatherDbModel = hourlyWeatherDao.getHourlyWeather()
        return mapper.mapHourlyDbModelListToHourlyEntityList(listOfHourlyWeatherDbModel)
    }

    override suspend fun loadData() {
        try {
            val weatherDto = apiService.getWeather()
            val currentWeather = mapper.mapCurrentDtoToCurrentDbModel(weatherDto.currentDto)
            val weekWeather = mapper.mapDailyDtoListToDailyDbModelList(weatherDto.dailyDto)
            val hourlyWeather = mapper.mapHourlyDtoListToHourlyDbModelList(weatherDto.hourlyDto)

            currentWeatherDao.clearCurrentWeather()
            currentWeatherDao.insertCurrentWeather(currentWeather)

            dailyWeatherDao.clearWeekWeather()
            dailyWeatherDao.insertWeekWeather(weekWeather)

            hourlyWeatherDao.clearHourlyWeather()
            hourlyWeatherDao.insertHourlyWeather(hourlyWeather)

        } catch (e: Exception) {

        }
    }
}