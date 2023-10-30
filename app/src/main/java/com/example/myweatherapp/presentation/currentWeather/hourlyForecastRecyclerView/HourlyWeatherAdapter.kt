package com.example.myweatherapp.presentation.currentWeather.hourlyForecastRecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.HourlyWeatherItemBinding
import com.example.myweatherapp.domain.HourlyWeatherEntity

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
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
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