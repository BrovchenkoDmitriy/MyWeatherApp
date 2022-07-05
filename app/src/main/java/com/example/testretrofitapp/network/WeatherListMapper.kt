//package com.example.testretrofitapp.network
//
//import android.annotation.SuppressLint
//import android.icu.text.SimpleDateFormat
//import com.example.testretrofitapp.domain.*
//
//
//class WeatherListMapper {
//
//    @SuppressLint("SimpleDateFormat")
//    private val formatForCurrentWeather = SimpleDateFormat("dd MMMM, HH:mm")
//
//    @SuppressLint("SimpleDateFormat")
//    //private val formatForDailyWeather = SimpleDateFormat("dd MMMM")
//
//    fun mapDtoToEntity(weatherDto: WeatherDto): WeatherEntity {
//        return WeatherEntity(
//            lat = weatherDto.lat,
//            lon = weatherDto.lon,
//            timezone = weatherDto.timezone,
//            timezoneOffset = weatherDto.timezoneOffset,
//            currentEntity = mapCurrentDtoToCurrentEntity(weatherDto.currentDto),
//            hourlyEntity = mapHourlyDtoListToHourlyEntityList(weatherDto.hourlyDto),
//            dailyEntity = mapDailyDtoListToDailyEntityList(weatherDto.dailyDto)
//        )
//    }
//
//    private fun mapCurrentDtoToCurrentEntity(currentWeatherDto: CurrentWeatherDto): CurrentWeatherEntity {
//        return CurrentWeatherEntity(
//            dt = formatForCurrentWeather.format(currentWeatherDto.dt.toLong() * 1000),
//            temp = currentWeatherDto.temp.substring(0, 2) + "\u00B0C",
//            feelsLike = currentWeatherDto.feelsLike.substring(0, 2) + "\u00B0C",
//            pressure = currentWeatherDto.pressure,
//            humidity = currentWeatherDto.humidity,
//            weather = mapWeatherTitleDtoToWeatherTitleEntity(currentWeatherDto.weather)
//        )
//    }
//
//    private fun mapHourlyDtoToHourlyEntity(hourlyWeatherDto: HourlyWeatherDto): HourlyWeatherEntity {
//        return HourlyWeatherEntity(
//            dt = hourlyWeatherDto.dt,
//            temp = hourlyWeatherDto.temp,
//            feelsLike = hourlyWeatherDto.feelsLike,
//            pressure = hourlyWeatherDto.pressure,
//            humidity = hourlyWeatherDto.humidity,
//            windSpeed = hourlyWeatherDto.windSpeed,
//            windDeg = hourlyWeatherDto.windDeg,
//            WindGust = hourlyWeatherDto.WindGust,
//            weather = mapWeatherTitleDtoToWeatherTitleEntity(hourlyWeatherDto.weather),
//            pop = hourlyWeatherDto.pop
//        )
//    }
//
//    private fun mapDailyDtoToDailyEntity(dailyWeatherDto: DailyWeatherDto): DailyWeatherEntity {
//        return DailyWeatherEntity(
//            dt = formatForCurrentWeather.format(dailyWeatherDto.dt.toLong() * 1000),
//            temp = mapTempDtoToTempEntity(dailyWeatherDto.temp),
//            feelsLike = mapTempDtoToTempEntity(dailyWeatherDto.feelsLike),
//            pressure = dailyWeatherDto.pressure,
//            humidity = dailyWeatherDto.humidity,
//            windSpeed = dailyWeatherDto.windSpeed,
//            windDeg = dailyWeatherDto.windDeg,
//            WindGust = dailyWeatherDto.WindGust,
//            weather = mapWeatherTitleDtoToWeatherTitleEntity(dailyWeatherDto.weather),
//            pop = dailyWeatherDto.pop
//        )
//    }
//
//    private fun mapWeatherTitleDtoToWeatherTitleEntity(
//        weatherTitleDto: List<WeatherTitleDto>
//    ): WeatherTitleEntity {
//        return WeatherTitleEntity(
//            id = weatherTitleDto[0].id,
//            main = weatherTitleDto[0].main,
//            description = weatherTitleDto[0].description.capitalize(),
//            icon = weatherTitleDto[0].icon
//        )
//    }
//
//    private fun mapTempDtoToTempEntity(dailyTempDto: DailyTempDto): DailyTempEntity {
//        return DailyTempEntity(
//            day = dailyTempDto.day.substring(0, 2) + "\u00B0C",
//            night = dailyTempDto.night.substring(0, 2) + "\u00B0C"
//        )
//    }
//
//    private fun mapDailyDtoListToDailyEntityList(list: List<DailyWeatherDto>) =
//        list.map {
//            mapDailyDtoToDailyEntity(it)
//        }
//
//    private fun mapHourlyDtoListToHourlyEntityList(list: List<HourlyWeatherDto>) =
//        list.map {
//            mapHourlyDtoToHourlyEntity(it)
//        }
//}