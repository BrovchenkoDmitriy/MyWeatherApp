package com.example.testretrofitapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.testretrofitapp.network.WeatherApi
import com.example.testretrofitapp.network.WeatherDto
//import com.example.testretrofitapp.network.WeatherForecastRepositoryImpl
import kotlinx.coroutines.launch

class OverViewModel(application: Application) : AndroidViewModel(application) {

  //  private val repository = WeatherForecastRepositoryImpl(application)
    // private val getWeatherEntityUseCase = GetWeatherEntityUseCase(repository)
    // private val loadDataUseCase = LoadDataUseCase(repository)

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status
    //  get() = _status

    private val _weatherDto = MutableLiveData<WeatherDto>()
    val weatherDto: LiveData<WeatherDto>
        get() = _weatherDto

    init {
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            try {
//               loadDataUseCase.invoke()
//               val listResult = getWeatherEntityUseCase.getWeatherEntity()
                val listResult = WeatherApi.retrofitService.getWeather()
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