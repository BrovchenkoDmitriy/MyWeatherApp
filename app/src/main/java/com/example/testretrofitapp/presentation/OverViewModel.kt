package com.example.testretrofitapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testretrofitapp.domain.GetWeatherEntityUseCase
import com.example.testretrofitapp.domain.LoadDataUseCase
import com.example.testretrofitapp.domain.WeatherEntity
import com.example.testretrofitapp.data.WeatherForecastRepositoryImpl
import kotlinx.coroutines.launch

class OverViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherForecastRepositoryImpl(application)
     private val getWeatherEntityUseCase = GetWeatherEntityUseCase(repository)
     private val loadDataUseCase = LoadDataUseCase(repository)

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status
    //  get() = _status

    private val _weatherDto = MutableLiveData<WeatherEntity>()
    val weatherDto: LiveData<WeatherEntity>
        get() = _weatherDto

    init {
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            try {
               loadDataUseCase.invoke()
               val listResult = getWeatherEntityUseCase.invoke()
              //  val listResult = WeatherApi.retrofitService.getWeather()
                Log.d("TAG", "Get WeatherDto $listResult")
                _weatherDto.value = listResult
                // _status.value = "Success: Weather for ${listResult.dailyDto.size} retrieved"
            } catch (e: Exception) {
                _weatherDto.value = null
                _status.value = "Failure: ${e.message}"
            }
        }
    }
}