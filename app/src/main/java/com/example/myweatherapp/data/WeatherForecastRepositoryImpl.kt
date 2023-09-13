package com.example.myweatherapp.data

import android.util.Log
import com.example.myweatherapp.Error
import com.example.myweatherapp.MyState
import com.example.myweatherapp.Success
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
    private val mapper: WeatherListMapper,
    private val currentWeatherDao: CurrentWeatherDao,
    private val dailyWeatherDao: DailyWeatherDao,
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val apiService: OpenWeatherAPi
) : WeatherForecastRepository {

    override suspend fun getCurrentWeather(): CurrentWeatherEntity {
        val currentWeatherDbModel = currentWeatherDao.getWeatherDbModel()
        return mapper.mapCurrentDbModelToCurrentEntity(currentWeatherDbModel)
    }

    override suspend fun getWeekWeather(): List<DailyWeatherEntity> {
        val listOfDailyWeatherDbModel = dailyWeatherDao.getWeatherWeek()

//  if i will use in fun of DAO interface LiveData<List<...>>  instead of List<...>
//        return Transformations.map(listOfDailyWeatherDbModel){
//            mapper.mapDailyDbModelListToDailyEntityList(it)
//        }

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
    ): MyState {

       return try {
            val weatherDto = apiService.getWeather(lat, lon, exclude, appid, units, lang)
            val currentWeather = mapper.mapCurrentDtoToCurrentDbModel(weatherDto)
            val weekWeather = mapper.mapDailyDtoListToDailyDbModelList(weatherDto.dailyDto)
            val hourlyWeather = mapper.mapHourlyDtoListToHourlyDbModelList(weatherDto.hourlyDto)

            currentWeatherDao.clearCurrentWeather()
            currentWeatherDao.insertCurrentWeather(currentWeather)

            dailyWeatherDao.clearWeekWeather()
            dailyWeatherDao.insertWeekWeather(weekWeather)

            hourlyWeatherDao.clearHourlyWeather()
            hourlyWeatherDao.insertHourlyWeather(hourlyWeather)
           
           Log.d("SERVER_RESPONSE", "Success ${weatherDto.currentDto.dt}")
            Success(
                mapper.mapCurrentDbModelToCurrentEntity(
                    mapper.mapCurrentDtoToCurrentDbModel(
                        weatherDto
                    )
                ),
                mapper.mapDailyDbModelListToDailyEntityList(
                    mapper.mapDailyDtoListToDailyDbModelList(
                        weatherDto.dailyDto
                    )
                ),
                mapper.mapHourlyDbModelListToHourlyEntityList(
                    mapper.mapHourlyDtoListToHourlyDbModelList(
                        weatherDto.hourlyDto
                    )
                )
            )

        } catch (e: Exception) {
            Error(e.localizedMessage ?: "Unknown exception")
        }
    }
}