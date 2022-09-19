package com.example.testretrofitapp.presentation.currentWeather.hourlyForecastRecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.testretrofitapp.R
import com.example.testretrofitapp.databinding.HourlyWeatherItemBinding
import com.example.testretrofitapp.domain.HourlyWeatherEntity

class HourlyWeatherAdapter :
    ListAdapter<HourlyWeatherEntity, HourlyWeatherViewHolder>(HourlyWeatherItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        Log.d("TAG", "onCreateViewHolder")
        val binding = HourlyWeatherItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourlyWeatherViewHolder(binding)
    }

    private fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
    }

    override fun onBindViewHolder(viewHolderHourly: HourlyWeatherViewHolder, position: Int) {
        val dailyWeather = getItem(position)
        val icon = dailyWeather.icon
        val imageIcon = "http://openweathermap.org/img/wn/$icon@2x.png"
        with(viewHolderHourly.binding){
            tvDataTime.text = dailyWeather.dt
            tvItemDayTemp.text = dailyWeather.temp
            bindImage(ivItemWeatherIcon,imageIcon)
        }
    }
}