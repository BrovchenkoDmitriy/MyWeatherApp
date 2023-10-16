package com.example.myweatherapp.presentation.weekForecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.domain.GetWeekWeatherUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeekForecastViewModel @Inject constructor(
    private val getWeekWeatherUseCase: GetWeekWeatherUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<WeekForecastState>()
    val state: LiveData<WeekForecastState> = _state

    fun updateData() {
        _state.value = Loading
        viewModelScope.launch {
            _state.value = Success(getWeekWeatherUseCase.invoke())
        }
    }
}