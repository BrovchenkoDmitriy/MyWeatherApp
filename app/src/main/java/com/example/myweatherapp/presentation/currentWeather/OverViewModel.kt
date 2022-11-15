package com.example.myweatherapp.presentation.currentWeather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.domain.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class OverViewModel @Inject constructor(private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
                                        private val getWeekWeatherUseCase: GetWeekWeatherUseCase,
                                        private val getHourlyWeatherUseCase: GeHourlyWeatherUseCase,
                                        private val loadDataUseCase: LoadDataUseCase)
                    : ViewModel() {

    private val _currentWeatherEntity = MutableLiveData<CurrentWeatherEntity>()
    val currentWeatherEntity: LiveData<CurrentWeatherEntity>
        get() = _currentWeatherEntity

    private val _weekWeatherEntity = MutableLiveData<List<DailyWeatherEntity>>()
    val weekWeatherEntity: LiveData<List<DailyWeatherEntity>>
        get() = _weekWeatherEntity

    private val _hourlyWeatherEntity = MutableLiveData<List<HourlyWeatherEntity>>()
    val hourlyWeatherEntity: LiveData<List<HourlyWeatherEntity>>
        get() = _hourlyWeatherEntity


    init {
        Log.d("TAG", "start init in OverViewModel.kt")
       // getWeather(url)
    }

    fun getWeather(lat: Double,
                   lon: Double,
                   exclude: String,
                   appid: String,
                   units: String,
                   lang: String) {
        viewModelScope.launch {
            loadDataUseCase(lat,
                lon,
                exclude,
                appid,
                units,
                lang)
            _weekWeatherEntity.value = getWeekWeatherUseCase.invoke()

            _currentWeatherEntity.value = getCurrentWeatherUseCase.invoke()

            _hourlyWeatherEntity.value = getHourlyWeatherUseCase.invoke()
        }
    }
}