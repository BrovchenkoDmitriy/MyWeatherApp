package com.example.myweatherapp.data

import android.icu.text.SimpleDateFormat
import com.example.myweatherapp.data.database.CurrentWeatherDbModel
import com.example.myweatherapp.data.database.DailyWeatherDbModel
import com.example.myweatherapp.data.database.HourlyWeatherDbModel
import com.example.myweatherapp.data.network.DailyWeatherDto
import com.example.myweatherapp.data.network.HourlyWeatherDto
import com.example.myweatherapp.data.network.WeatherDto
import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.DailyWeatherEntity
import com.example.myweatherapp.domain.HourlyWeatherEntity
import java.util.Locale
import javax.inject.Inject


class WeatherListMapper @Inject constructor() {

    private val language = Locale.getDefault()
    private val formatForCurrentWeather = SimpleDateFormat("(HH:mm)", language)
    private val formatForDailyWeather = SimpleDateFormat("EEEE, dd MMMM", language)
    private val formatForHourlyWeather = SimpleDateFormat("dd.MM HH:mm", language)

    private fun String.myCapitalize(): String {
        return if (this.isNotEmpty()) {
            return if (this[0].isLowerCase()) {
                this[0].titlecase(Locale.getDefault()) + this.substring(1)
            } else this
        } else this
    }

////////////////////////////////////  DtoToDbModel  ////////////////////////////////////////////////

    fun mapCurrentDtoToCurrentDbModel(weatherDto: WeatherDto): CurrentWeatherDbModel {
        return CurrentWeatherDbModel(
            id = 0,
            dt = formatForCurrentWeather.format(weatherDto.currentDto.dt.toLong() * 1000),
            temp = "${weatherDto.currentDto.temp.toDouble().toInt()}°C",
            feelsLike = "${weatherDto.currentDto.feelsLike.toDouble().toInt()}\u00B0C",
            pressure = weatherDto.currentDto.pressure,
            humidity = weatherDto.currentDto.humidity,
            windSpeed = weatherDto.currentDto.windSpeed,
            windGust = weatherDto.currentDto.windGust,
            windDeg = weatherDto.currentDto.windDeg,
            description = weatherDto.currentDto.weather[0].description.myCapitalize(),
            icon = weatherDto.currentDto.weather[0].icon,
            lat = weatherDto.lat.toDouble(),
            lon = weatherDto.lon.toDouble()
        )
    }

    private fun mapHourlyDtoToHourlyDbModel(hourlyWeatherDto: HourlyWeatherDto): HourlyWeatherDbModel {
        return HourlyWeatherDbModel(
            id = 0,
            dt = formatForHourlyWeather.format(hourlyWeatherDto.dt.toLong() * 1000).myCapitalize(),
            temp = "${hourlyWeatherDto.temp.toDouble().toInt()}°C",
            feelsLike = "${hourlyWeatherDto.feelsLike.toDouble().toInt()}°C",
            pressure = hourlyWeatherDto.pressure,
            humidity = hourlyWeatherDto.humidity,
            windSpeed = hourlyWeatherDto.windSpeed,
            windGust = hourlyWeatherDto.windGust,
            windDeg = hourlyWeatherDto.windDeg,
            description = hourlyWeatherDto.weather[0].description.myCapitalize(),
            icon = hourlyWeatherDto.weather[0].icon,
            pop = hourlyWeatherDto.pop
        )
    }

    private fun mapDailyDtoToDailyDbModel(dailyWeatherDto: DailyWeatherDto): DailyWeatherDbModel {
        return DailyWeatherDbModel(
            id = 0,
            dt = formatForDailyWeather.format(dailyWeatherDto.dt.toLong() * 1000)
                .myCapitalize(),
            tempDay ="${dailyWeatherDto.temp.day.toDouble().toInt()}°C",
            tempNight = "${dailyWeatherDto.temp.night.toDouble().toInt()}\u00B0",
            feelsLikeDay = "${dailyWeatherDto.feelsLike.day.toDouble().toInt()}\u00B0",
            feelsLikeNight = "${dailyWeatherDto.feelsLike.night.toDouble().toInt()}\u00B0",
            pressure = dailyWeatherDto.pressure,
            humidity = dailyWeatherDto.humidity,
            windSpeed = dailyWeatherDto.windSpeed,
            windGust = dailyWeatherDto.windGust,
            windDeg = dailyWeatherDto.windDeg,
            description = dailyWeatherDto.weather[0].description.myCapitalize(),
            icon = dailyWeatherDto.weather[0].icon,
            pop = dailyWeatherDto.pop
        )
    }

    fun mapDailyDtoListToDailyDbModelList(list: List<DailyWeatherDto>) =
        list.map {
            mapDailyDtoToDailyDbModel(it)
        }

    fun mapHourlyDtoListToHourlyDbModelList(list: List<HourlyWeatherDto>) =
        list.map {
            mapHourlyDtoToHourlyDbModel(it)
        }

////////////////////////////////////  DbModelToEntity  /////////////////////////////////////////////

    fun mapCurrentDbModelToCurrentEntity(currentWeatherDbModel: CurrentWeatherDbModel): CurrentWeatherEntity {
        return CurrentWeatherEntity(
            id = currentWeatherDbModel.id,
            dt = currentWeatherDbModel.dt,
            temp = currentWeatherDbModel.temp,
            feelsLike = currentWeatherDbModel.feelsLike,
            pressure = currentWeatherDbModel.pressure,
            humidity = currentWeatherDbModel.humidity,
            windSpeed = currentWeatherDbModel.windSpeed,
            windGust = currentWeatherDbModel.windGust,
            windDeg = currentWeatherDbModel.windDeg,
            description = currentWeatherDbModel.description,
            icon = currentWeatherDbModel.icon,
            lat = currentWeatherDbModel.lat,
            lon = currentWeatherDbModel.lon
        )
    }

    private fun mapHourlyDbModelToHourlyEntity(hourlyWeatherDbModel: HourlyWeatherDbModel): HourlyWeatherEntity {
        return HourlyWeatherEntity(
            id = hourlyWeatherDbModel.id,
            dt = hourlyWeatherDbModel.dt,
            temp = hourlyWeatherDbModel.temp,
            feelsLike = hourlyWeatherDbModel.feelsLike,
            pressure = hourlyWeatherDbModel.pressure,
            humidity = hourlyWeatherDbModel.humidity,
            windSpeed = hourlyWeatherDbModel.windSpeed,
            windGust = hourlyWeatherDbModel.windGust,
            windDeg = hourlyWeatherDbModel.windDeg,
            description = hourlyWeatherDbModel.description,
            icon = hourlyWeatherDbModel.icon,
            pop = hourlyWeatherDbModel.pop
        )
    }

    private fun mapDailyDbModelToDailyEntity(dailyWeatherDbModel: DailyWeatherDbModel): DailyWeatherEntity {
        return DailyWeatherEntity(
            id = dailyWeatherDbModel.id,
            dt = dailyWeatherDbModel.dt,
            tempDay = dailyWeatherDbModel.tempDay,
            tempNight = dailyWeatherDbModel.tempNight,
            feelsLikeDay = dailyWeatherDbModel.feelsLikeDay,
            feelsLikeNight = dailyWeatherDbModel.feelsLikeNight,
            pressure = dailyWeatherDbModel.pressure,
            humidity = dailyWeatherDbModel.humidity,
            windSpeed = dailyWeatherDbModel.windSpeed,
            windGust = dailyWeatherDbModel.windGust,
            windDeg = dailyWeatherDbModel.windDeg,
            description = dailyWeatherDbModel.description,
            icon = dailyWeatherDbModel.icon,
            pop = ((dailyWeatherDbModel.pop).toDouble()*1000).toInt()/10
        )
    }

    fun mapDailyDbModelListToDailyEntityList(list: List<DailyWeatherDbModel>) =
        list.map {
            mapDailyDbModelToDailyEntity(it)
        }

    fun mapHourlyDbModelListToHourlyEntityList(list: List<HourlyWeatherDbModel>) =
        list.map {
            mapHourlyDbModelToHourlyEntity(it)
        }
}