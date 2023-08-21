package com.example.myweatherapp.presentation.weekForecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.domain.DailyWeatherEntity
import com.example.myweatherapp.domain.GetWeekWeatherUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeekForecastViewModel @Inject constructor(
    private val getWeekWeatherUseCase: GetWeekWeatherUseCase,
) : ViewModel() {

    private var _weekWeather = MutableLiveData<List<DailyWeatherEntity>>()
    val weekWeather: LiveData<List<DailyWeatherEntity>>
        get() = _weekWeather

    fun clearLiveData() {
        _weekWeather.value = listOf()
    }

    fun updateData() {
        viewModelScope.launch {
            _weekWeather.value = getWeekWeatherUseCase.invoke()
        }
    }
}
