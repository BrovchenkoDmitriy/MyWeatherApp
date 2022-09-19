package com.example.testretrofitapp.presentation.weekForecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testretrofitapp.domain.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeekForecastViewModel @Inject constructor(
    private val getWeekWeatherUseCase: GetWeekWeatherUseCase,
) : ViewModel() {

    private val _weekWeatherDto = MutableLiveData<List<DailyWeatherEntity>>()
    val weekWeatherDto: LiveData<List<DailyWeatherEntity>>
        get() = _weekWeatherDto

    init {
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            _weekWeatherDto.value = getWeekWeatherUseCase.invoke()
        }
    }
}