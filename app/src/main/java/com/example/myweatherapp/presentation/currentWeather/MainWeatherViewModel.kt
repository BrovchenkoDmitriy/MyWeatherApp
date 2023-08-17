package com.example.myweatherapp.presentation.currentWeather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.domain.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainWeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getWeekWeatherUseCase: GetWeekWeatherUseCase,
    private val getHourlyWeatherUseCase: GeHourlyWeatherUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val getSearchedCitiesUseCase: GetSearchedCitiesUseCase
) : ViewModel() {

    private val _currentWeatherEntity = MutableLiveData<CurrentWeatherEntity>()
    val currentWeatherEntity: LiveData<CurrentWeatherEntity>
        get() = _currentWeatherEntity

    private val _hourlyWeatherEntity = MutableLiveData<List<HourlyWeatherEntity>>()
    val hourlyWeatherEntity: LiveData<List<HourlyWeatherEntity>>
        get() = _hourlyWeatherEntity

    private val _searchedCities = MutableLiveData<List<SearchedCities>>()
    val searchedCities: LiveData<List<SearchedCities>> = _searchedCities

    fun clearLiveData() {
        _currentWeatherEntity.value = null
        _hourlyWeatherEntity.value = null
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
            loadDataUseCase(lat, lon, exclude, appid, units, lang)
            _currentWeatherEntity.value = getCurrentWeatherUseCase.invoke()
            _hourlyWeatherEntity.value = getHourlyWeatherUseCase.invoke()
        }
    }

    fun searchCities(
        query: String,
    ) {
        viewModelScope.launch {
            val result = getSearchedCitiesUseCase.invoke(query)
            _searchedCities.value = result
            Log.d("SEARCH_CITIES", "ViewModel: " + result.toString())
        }
    }
}