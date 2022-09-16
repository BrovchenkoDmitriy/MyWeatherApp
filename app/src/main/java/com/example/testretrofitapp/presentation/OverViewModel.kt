package com.example.testretrofitapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testretrofitapp.domain.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class OverViewModel @Inject constructor(private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
                                        private val getWeekWeatherUseCase: GetWeekWeatherUseCase,
                                        private val getHourlyWeatherUseCase: GeHourlyWeatherUseCase,
                                        private val loadDataUseCase: LoadDataUseCase)
                    : ViewModel() {

    private val _currentWeatherDto = MutableLiveData<CurrentWeatherEntity>()
    val currentWeatherDto: LiveData<CurrentWeatherEntity>
        get() = _currentWeatherDto

    private val _weekWeatherDto = MutableLiveData<List<DailyWeatherEntity>>()
    val weekWeatherDto: LiveData<List<DailyWeatherEntity>>
        get() = _weekWeatherDto

    private val _hourlyWeatherDto = MutableLiveData<List<HourlyWeatherEntity>>()
    val hourlyWeatherDto: LiveData<List<HourlyWeatherEntity>>
        get() = _hourlyWeatherDto


    init {
        Log.d("TAG", "start init in OverViewModel.kt")
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            loadDataUseCase()
            _weekWeatherDto.value = getWeekWeatherUseCase.invoke()

            _currentWeatherDto.value = getCurrentWeatherUseCase.invoke()

            _hourlyWeatherDto.value = getHourlyWeatherUseCase.invoke()
        }
    }
}