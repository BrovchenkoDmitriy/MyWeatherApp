package com.example.myweatherapp.presentation.googleMap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.GetCurrentWeatherUseCase
import com.example.myweatherapp.domain.LoadDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val loadDataUseCase: LoadDataUseCase
) : ViewModel() {

    private val _currentWeatherEntity = MutableLiveData<CurrentWeatherEntity>()
    val currentWeatherDto: LiveData<CurrentWeatherEntity>
        get() = _currentWeatherEntity

    fun getCurrentWeather(): CurrentWeatherEntity {
        return currentWeatherDto.value ?: throw RuntimeException("Data not exist")
    }

fun uploadCurrentWeather(){
    viewModelScope.launch {
        _currentWeatherEntity.value = getCurrentWeatherUseCase.invoke()
    }
}

     fun getWeather(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        units: String,
        lang: String
    ) {
        viewModelScope.launch {
            loadDataUseCase.invoke(lat, lon, exclude, appid, units, lang)
            _currentWeatherEntity.value = getCurrentWeatherUseCase.invoke()
        }
    }
}