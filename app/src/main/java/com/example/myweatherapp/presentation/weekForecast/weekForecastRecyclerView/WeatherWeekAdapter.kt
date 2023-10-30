package com.example.myweatherapp.presentation.weekForecast.weekForecastRecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.WeekWeatherItemBinding
import com.example.myweatherapp.domain.DailyWeatherEntity

class WeatherWeekAdapter :
    ListAdapter<DailyWeatherEntity, WeatherViewHolder>(WeatherDailyItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        Log.d("TAG", "onCreateViewHolder")
        val binding =
            WeekWeatherItemBinding.inflate( //заменили с WeekWeatherItemBinding на WeekWeatherItemSecondBinding
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
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: WeatherViewHolder, position: Int) {
        val dailyWeather = getItem(position)
        val icon = dailyWeather.icon
        val imageIcon = "http://openweathermap.org/img/wn/$icon@2x.png"
        with(viewHolder.binding) {
            tvDataTime.text = dailyWeather.dt
            tvItemDescription.text = dailyWeather.description
            tvItemDayTemp.text = dailyWeather.tempDay
            tvItemNightTemp.text = dailyWeather.tempNight
            val pop = dailyWeather.pop
            if (pop != 0) {
                tvPop.visibility = View.VISIBLE
                val popString = "$pop%"
                tvPop.text = popString
            }
            bindImage(ivItemWeatherIcon, imageIcon)

//            root.setOnClickListener {
//            if(llHourlyWeather.visibility == View.GONE)llHourlyWeather.visibility = View.VISIBLE
//                else llHourlyWeather.visibility = View.GONE
//            }
        }
    }
}