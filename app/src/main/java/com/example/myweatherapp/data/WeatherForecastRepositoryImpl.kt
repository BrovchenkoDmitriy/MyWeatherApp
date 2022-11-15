package com.example.myweatherapp.data

import android.util.Log
import com.example.myweatherapp.data.database.CurrentWeatherDao
import com.example.myweatherapp.data.database.DailyWeatherDao
import com.example.myweatherapp.data.database.HourlyWeatherDao
import com.example.myweatherapp.data.network.OpenWeatherAPi
import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.DailyWeatherEntity
import com.example.myweatherapp.domain.HourlyWeatherEntity
import com.example.myweatherapp.domain.WeatherForecastRepository
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

    override suspend fun loadData(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        units: String,
        lang: String
    ) {

        try {
            Log.d("TAGA", "try make retrofit")
        //      val weatherDto = getData(lat, lon, exclude, appid, units, lang)

            val weatherDto = apiService.getWeather(lat, lon, exclude, appid, units, lang)
            Log.d("TAGA", weatherDto.currentDto.temp)
            Log.d("TAGA", weatherDto.toString())
            val currentWeather = mapper.mapCurrentDtoToCurrentDbModel(weatherDto.currentDto)
            Log.d("TAGA", currentWeather.temp)
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