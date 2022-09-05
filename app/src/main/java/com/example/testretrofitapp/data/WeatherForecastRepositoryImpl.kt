package com.example.testretrofitapp.data

import android.util.Log
import com.example.testretrofitapp.data.database.CurrentWeatherDao
import com.example.testretrofitapp.data.database.DailyWeatherDao
import com.example.testretrofitapp.data.database.HourlyWeatherDao
import com.example.testretrofitapp.data.network.OpenWeatherAPi
import com.example.testretrofitapp.domain.CurrentWeatherEntity
import com.example.testretrofitapp.domain.DailyWeatherEntity
import com.example.testretrofitapp.domain.HourlyWeatherEntity
import com.example.testretrofitapp.domain.WeatherForecastRepository
import javax.inject.Inject

class WeatherForecastRepositoryImpl @Inject constructor(
    //private val application: Application,
    private val mapper: WeatherListMapper,
    private val currentWeatherDao: CurrentWeatherDao,
    private val dailyWeatherDao: DailyWeatherDao,
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val apiService: OpenWeatherAPi
) : WeatherForecastRepository {
//    private val db = AppDataBase.getInstance(application)
//    private val currentWeatherDao = db.currentWeatherDao()
//    private val dailyWeatherDao = db.dailyWeatherDao()
//    private val hourlyWeatherDao = db.hourlyWeatherDao()
//    private val apiService = WeatherApi.retrofitService

    override suspend fun getCurrentWeather(): CurrentWeatherEntity {
        val currentWeatherDbModel = currentWeatherDao.getWeatherDbModel()
        return mapper.mapCurrentDbModelToCurrentEntity(currentWeatherDbModel)
    }

    override suspend fun getWeekWeather(): List<DailyWeatherEntity> {
        val listOfDailyWeatherDbModel = dailyWeatherDao.getWeatherWeek()
        return mapper.mapDailyDbModelListToDailyEntityList(listOfDailyWeatherDbModel)
    }

    override suspend fun getHourlyWeather(): List<HourlyWeatherEntity> {
        val listOfHourlyWeatherDbModel = hourlyWeatherDao.getHourlyWeather()
        return mapper.mapHourlyDbModelListToHourlyEntityList(listOfHourlyWeatherDbModel)
    }

    override suspend fun loadData() {
        try {
            val weatherDto = apiService.getWeather()
            Log.d("TAG", weatherDto.currentDto.temp)
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
            Log.d("TAG", e.stackTraceToString())
        }
    }
}