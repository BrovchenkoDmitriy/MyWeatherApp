package com.example.myweatherapp.presentation.currentWeather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.Loading
import com.example.myweatherapp.MyState
import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.GeHourlyWeatherUseCase
import com.example.myweatherapp.domain.GetCurrentWeatherUseCase
import com.example.myweatherapp.domain.GetSearchedCitiesUseCase
import com.example.myweatherapp.domain.HourlyWeatherEntity
import com.example.myweatherapp.domain.LoadDataUseCase
import com.example.myweatherapp.domain.SearchedCities
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainWeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
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

    private val _state = MutableLiveData<MyState>()
    val state: LiveData<MyState> = _state

    fun getNewWeather(
        lat: Double,
        lon: Double,
        exclude: String,
        appid: String,
        units: String,
        lang: String
    ) {
        _state.value = Loading
        viewModelScope.launch {
            val state = loadDataUseCase(lat, lon, exclude, appid, units, lang)
            _state.value = state

        }
    }

    fun getWeather() {
        viewModelScope.launch {
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
        }
    }
}