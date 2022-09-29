package com.example.testretrofitapp.presentation.googleMap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testretrofitapp.domain.CurrentWeatherEntity
import com.example.testretrofitapp.domain.GetCurrentWeatherUseCase
import com.example.testretrofitapp.domain.LoadDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val loadDataUseCase: LoadDataUseCase
) : ViewModel() {

    private val _currentWeatherDto = MutableLiveData<CurrentWeatherEntity>()
    val currentWeatherDto: LiveData<CurrentWeatherEntity>
        get() = _currentWeatherDto

//    fun getCurrentWeather(): CurrentWeatherEntity {
//        return currentWeatherDto.value ?: throw RuntimeException("Data not exist")
//    }

    // init{
    //getWeather(location.value?.latitude?:0.0, location.value?.longitude?:0.0)
    //  }

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
            _currentWeatherDto.value = getCurrentWeatherUseCase.invoke()

        }
    }
}