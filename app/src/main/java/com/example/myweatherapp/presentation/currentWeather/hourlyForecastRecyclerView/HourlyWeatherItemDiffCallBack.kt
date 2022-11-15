package com.example.myweatherapp.presentation.currentWeather.hourlyForecastRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherapp.domain.HourlyWeatherEntity


class HourlyWeatherItemDiffCallBack:DiffUtil.ItemCallback<HourlyWeatherEntity>() {
    override fun areItemsTheSame(oldWeatherItem: HourlyWeatherEntity, newWeatherItem: HourlyWeatherEntity): Boolean {
        return oldWeatherItem.id == newWeatherItem.id
    }

    override fun areContentsTheSame(oldWeatherItem: HourlyWeatherEntity, newWeatherItem: HourlyWeatherEntity): Boolean {
        return oldWeatherItem == newWeatherItem
    }
}