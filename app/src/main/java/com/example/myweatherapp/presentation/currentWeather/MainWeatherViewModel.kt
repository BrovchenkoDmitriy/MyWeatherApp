package com.example.myweatherapp.presentation.currentWeather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.domain.CurrentWeatherEntity
import com.example.myweatherapp.domain.GeHourlyWeatherUseCase
import com.example.myweatherapp.domain.GetCurrentWeatherUseCase
import com.example.myweatherapp.domain.GetSearchedCitiesUseCase
import com.example.myweatherapp.domain.HourlyWeatherEntity
import com.example.myweatherapp.domain.LoadDataUseCase
import com.example.myweatherapp.domain.ResponseError
import com.example.myweatherapp.domain.ResponseSuccess
import com.example.myweatherapp.domain.SearchedCities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
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

    private val _state = MutableLiveData<CurrentWeatherState>()
    val state: LiveData<CurrentWeatherState> = _state

    val searchCityState: MutableSharedFlow<String> = MutableSharedFlow()

    init {
        subscribeToSearchQueryChanges()
    }

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
            when (val result = loadDataUseCase(lat, lon, exclude, appid, units, lang)) {
                is ResponseSuccess -> _state.value = Success
                is ResponseError -> _state.value = Error(result.error)
            }
        }
    }

    fun getWeather() {
        viewModelScope.launch {
            _currentWeatherEntity.value = getCurrentWeatherUseCase.invoke()
            _hourlyWeatherEntity.value = getHourlyWeatherUseCase.invoke()
        }
    }

    fun changeStateStatus() {
        _state.value = Success
    }

    private fun searchCities(
        query: String,
    ) {
        viewModelScope.launch {
            val result = getSearchedCitiesUseCase.invoke(query)
            _searchedCities.value = result
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun subscribeToSearchQueryChanges() {
        searchCityState
            .debounce(1000L)
            .mapLatest { query -> searchCities(query) }
            .launchIn(viewModelScope)
    }
}