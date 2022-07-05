package com.example.testretrofitapp.presentation

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testretrofitapp.R
import com.example.testretrofitapp.databinding.WeekWeatherItemBinding
import com.example.testretrofitapp.network.DailyWeatherDto

class WeatherWeekAdapter : RecyclerView.Adapter<WeatherViewHolder>() {

    var weatherList = listOf<DailyWeatherDto>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        Log.d("TAG", "onCreateViewHolder")
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.week_weather_item,
                parent,
                false
            )
        //val tvDataTime = view.findViewById<TextView>(R.id.tv_dataTime)
        //other textview and imageview
        val binding = WeekWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }
    private val format = SimpleDateFormat("dd MMMM")

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
    }
    override fun onBindViewHolder(viewHolder: WeatherViewHolder, position: Int) {


        val dailyWeather = //getItemId(position)
            weatherList[position]
        //Log.d("TAG", "onBindViewHolder ${dailyWeather.temp.night} ")
        //val binding = viewHolder.binding
        viewHolder.tvDataTime.text = format.format(dailyWeather.dt.toLong()*1000)
        viewHolder.tvItemDescription.text = dailyWeather.weather[0].description
        viewHolder.tvItemDayTemp.text = dailyWeather.temp.day.substring(0, 2) + "\u00B0"
        viewHolder.tvItemNightTemp.text = dailyWeather.temp.night.substring(0, 2) + "\u00B0"
        val icon = dailyWeather.weather[0].icon
        val imageIcon = "http://openweathermap.org/img/wn/$icon@2x.png"
        bindImage(viewHolder.ivItemWeatherIcon, imageIcon)


    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

}