package com.example.testretrofitapp.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testretrofitapp.R
import com.example.testretrofitapp.databinding.WeekWeatherItemBinding
import com.example.testretrofitapp.domain.DailyWeatherEntity

class WeatherWeekAdapter : RecyclerView.Adapter<WeatherViewHolder>() {

    var weatherList = listOf<DailyWeatherEntity>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        Log.d("TAG", "onCreateViewHolder")
        val binding = WeekWeatherItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherViewHolder(binding)
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

    override fun onBindViewHolder(viewHolder: WeatherViewHolder, position: Int) {
        val dailyWeather = weatherList[position]
        val icon = dailyWeather.icon
        val imageIcon = "http://openweathermap.org/img/wn/$icon@2x.png"
        with(viewHolder.binding){
            tvDataTime.text = dailyWeather.dt
            tvItemDescription.text = dailyWeather.description
            tvItemDayTemp.text = dailyWeather.tempDay
            tvItemNightTemp.text = dailyWeather.tempNight
            bindImage(ivItemWeatherIcon,imageIcon)
        }
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

}