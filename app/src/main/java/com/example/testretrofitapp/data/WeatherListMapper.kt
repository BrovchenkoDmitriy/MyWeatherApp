package com.example.testretrofitapp.data

import android.icu.text.SimpleDateFormat
import com.example.testretrofitapp.data.database.*
import com.example.testretrofitapp.data.network.*
import com.example.testretrofitapp.domain.*
import java.util.*


class WeatherListMapper {

    private val language = Locale.getDefault()
    private val formatForCurrentWeather = SimpleDateFormat("(HH:mm)", language)
    private val formatForDailyWeather = SimpleDateFormat("EEEE, dd MMMM", language)

////////////////////////////////////  DtoToDbModel  ////////////////////////////////////////////////

    fun mapDtoToDbModel(weatherDto: WeatherDto): WeatherDBModel {
        return WeatherDBModel(
            lat = weatherDto.lat,
            lon = weatherDto.lon,
            timezone = weatherDto.timezone,
            timezoneOffset = weatherDto.timezoneOffset,
            currentDao = mapCurrentDtoToCurrentDao(weatherDto.currentDto),
            hourlyDao = mapHourlyDtoListToHourlyDaoList(weatherDto.hourlyDto),
            dailyDao = mapDailyDtoListToDailyDaoList(weatherDto.dailyDto)
        )
    }

    private fun mapCurrentDtoToCurrentDao(currentWeatherDto: CurrentWeatherDto): CurrentWeatherDao {
        return CurrentWeatherDao(
            dt = formatForCurrentWeather.format(currentWeatherDto.dt.toLong() * 1000),
            temp = currentWeatherDto.temp.substring(0, 2) + "\u00B0C",
            feelsLike = currentWeatherDto.feelsLike.substring(0, 2) + "\u00B0C",
            pressure = currentWeatherDto.pressure,
            humidity = currentWeatherDto.humidity,
            windSpeed =currentWeatherDto.windSpeed,
            windGust =currentWeatherDto.windGust,
            windDeg =currentWeatherDto.windDeg,
            weather = mapWeatherTitleDtoToWeatherTitleDao(currentWeatherDto.weather)
        )
    }

    private fun mapHourlyDtoToHourlyDao(hourlyWeatherDto: HourlyWeatherDto): HourlyWeatherDao {
        return HourlyWeatherDao(
            dt = hourlyWeatherDto.dt,
            temp = hourlyWeatherDto.temp,
            feelsLike = hourlyWeatherDto.feelsLike,
            pressure = hourlyWeatherDto.pressure,
            humidity = hourlyWeatherDto.humidity,
            windSpeed = hourlyWeatherDto.windSpeed,
            windGust = hourlyWeatherDto.windGust,
            windDeg = hourlyWeatherDto.windDeg,
            weather = mapWeatherTitleDtoToWeatherTitleDao(hourlyWeatherDto.weather),
            pop = hourlyWeatherDto.pop
        )
    }

    private fun mapDailyDtoToDailyDao(dailyWeatherDto: DailyWeatherDto): DailyWeatherDao {
        return DailyWeatherDao(
            dt = formatForDailyWeather.format(dailyWeatherDto.dt.toLong() * 1000)
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                },
            temp = mapTempDtoToTempDao(dailyWeatherDto.temp),
            feelsLike = mapTempDtoToTempDao(dailyWeatherDto.feelsLike),
            pressure = dailyWeatherDto.pressure,
            humidity = dailyWeatherDto.humidity,
            windSpeed = dailyWeatherDto.windSpeed,
            windGust = dailyWeatherDto.windGust,
            windDeg = dailyWeatherDto.windDeg,
            weather = mapWeatherTitleDtoToWeatherTitleDao(dailyWeatherDto.weather),
            pop = dailyWeatherDto.pop
        )
    }

    private fun mapWeatherTitleDtoToWeatherTitleDao(
        weatherTitleDto: List<WeatherTitleDto>
    ): WeatherTitleDao {
        return WeatherTitleDao(
            id = weatherTitleDto[0].id,
            main = weatherTitleDto[0].main,
            description = weatherTitleDto[0].description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            },
            icon = weatherTitleDto[0].icon
        )
    }

    private fun mapTempDtoToTempDao(dailyTempDto: DailyTempDto): DailyTempDao {
        return DailyTempDao(
            day = dailyTempDto.day.substring(0, 2) + "\u00B0",
            night = dailyTempDto.night.substring(0, 2) + "\u00B0"
        )
    }

    private fun mapDailyDtoListToDailyDaoList(list: List<DailyWeatherDto>) =
        list.map {
            mapDailyDtoToDailyDao(it)
        }

    private fun mapHourlyDtoListToHourlyDaoList(list: List<HourlyWeatherDto>) =
        list.map {
            mapHourlyDtoToHourlyDao(it)
        }


////////////////////////////////////  DbModelToEntity  /////////////////////////////////////////////


    fun mapDbModelToEntity(weatherDao: WeatherDBModel): WeatherEntity {
        return WeatherEntity(
            lat = weatherDao.lat,
            lon = weatherDao.lon,
            timezone = weatherDao.timezone,
            timezoneOffset = weatherDao.timezoneOffset,
            currentEntity = mapCurrentDaoToCurrentEntity(weatherDao.currentDao),
            hourlyEntity = mapHourlyDaoListToHourlyEntityList(weatherDao.hourlyDao),
            dailyEntity = mapDailyDaoListToDailyEntityList(weatherDao.dailyDao)
        )
    }

    private fun mapCurrentDaoToCurrentEntity(currentWeatherDao: CurrentWeatherDao): CurrentWeatherEntity {
        return CurrentWeatherEntity(
            dt = currentWeatherDao.dt, //formatForCurrentWeather.format(currentWeatherDao.dt.toLong() * 1000),
            temp = currentWeatherDao.temp,//.substring(0, 2) + "\u00B0C",
            feelsLike = currentWeatherDao.feelsLike,//.substring(0, 2) + "\u00B0C",
            pressure = currentWeatherDao.pressure,
            humidity = currentWeatherDao.humidity,
            windSpeed = currentWeatherDao.windSpeed,
            windGust = currentWeatherDao.windGust,
            windDeg = currentWeatherDao.windDeg,
            weather = mapWeatherTitleDaoToWeatherTitleEntity(currentWeatherDao.weather)
        )
    }

    private fun mapHourlyDaoToHourlyEntity(hourlyWeatherDao: HourlyWeatherDao): HourlyWeatherEntity {
        return HourlyWeatherEntity(
            dt = hourlyWeatherDao.dt,
            temp = hourlyWeatherDao.temp,
            feelsLike = hourlyWeatherDao.feelsLike,
            pressure = hourlyWeatherDao.pressure,
            humidity = hourlyWeatherDao.humidity,
            windSpeed = hourlyWeatherDao.windSpeed,
            windGust = hourlyWeatherDao.windGust,
            windDeg = hourlyWeatherDao.windDeg,
            weather = mapWeatherTitleDaoToWeatherTitleEntity(hourlyWeatherDao.weather),
            pop = hourlyWeatherDao.pop
        )
    }

    private fun mapDailyDaoToDailyEntity(dailyWeatherDao: DailyWeatherDao): DailyWeatherEntity {
        return DailyWeatherEntity(
            dt = dailyWeatherDao.dt,
//                formatForDailyWeather.format(dailyWeatherDao.dt.toLong() * 1000)
//                .replaceFirstChar {
//                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
//                },
            temp = mapTempDaoToTempEntity(dailyWeatherDao.temp),
            feelsLike = mapTempDaoToTempEntity(dailyWeatherDao.feelsLike),
            pressure = dailyWeatherDao.pressure,
            humidity = dailyWeatherDao.humidity,
            windSpeed = dailyWeatherDao.windSpeed,
            windGust = dailyWeatherDao.windGust,
            windDeg = dailyWeatherDao.windDeg,
            weather = mapWeatherTitleDaoToWeatherTitleEntity(dailyWeatherDao.weather),
            pop = dailyWeatherDao.pop
        )
    }

    private fun mapWeatherTitleDaoToWeatherTitleEntity(
        weatherTitleDao: WeatherTitleDao
    ): WeatherTitleEntity {
        return WeatherTitleEntity(
            id = weatherTitleDao.id,//[0].id,
            main = weatherTitleDao.main,//[0].main,
            description = weatherTitleDao.description,//[0].description.replaceFirstChar {
//                if (it.isLowerCase()) it.titlecase(
//                    Locale.getDefault()
//                ) else it.toString()
//            },
            icon = weatherTitleDao.icon//[0].icon
        )
    }

    private fun mapTempDaoToTempEntity(dailyTempDao: DailyTempDao): DailyTempEntity {
        return DailyTempEntity(
            day = dailyTempDao.day,//.substring(0, 2) + "\u00B0",
            night = dailyTempDao.night,//.substring(0, 2) + "\u00B0"
        )
    }

    private fun mapDailyDaoListToDailyEntityList(list: List<DailyWeatherDao>) =
        list.map {
            mapDailyDaoToDailyEntity(it)
        }

    private fun mapHourlyDaoListToHourlyEntityList(list: List<HourlyWeatherDao>) =
        list.map {
            mapHourlyDaoToHourlyEntity(it)
        }
}