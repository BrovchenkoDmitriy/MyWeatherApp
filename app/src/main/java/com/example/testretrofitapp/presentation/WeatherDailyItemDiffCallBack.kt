package com.example.testretrofitapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.testretrofitapp.domain.DailyWeatherEntity


class WeatherDailyItemDiffCallBack():DiffUtil.ItemCallback<DailyWeatherEntity>() {
    override fun areItemsTheSame(oldWeatherItem: DailyWeatherEntity, newWeatherItem: DailyWeatherEntity): Boolean {
        return oldWeatherItem.id == newWeatherItem.id
    }

    override fun areContentsTheSame(oldWeatherItem: DailyWeatherEntity, newWeatherItem: DailyWeatherEntity): Boolean {
        return oldWeatherItem == newWeatherItem
    }
}