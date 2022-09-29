package com.example.testretrofitapp.presentation.currentWeather

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.testretrofitapp.R
import com.example.testretrofitapp.WeatherApp
import com.example.testretrofitapp.databinding.FragmentOverviewBinding
import com.example.testretrofitapp.presentation.ViewModelFactory
import com.example.testretrofitapp.presentation.currentWeather.hourlyForecastRecyclerView.HourlyWeatherAdapter
import com.example.testretrofitapp.presentation.weekForecast.weekForecastRecyclerView.WeatherWeekAdapter
import java.util.*
import javax.inject.Inject


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding: FragmentOverviewBinding
        get() = _binding ?: throw RuntimeException("FragmentOverviewBinding is null")

    private lateinit var weatherAdapter: WeatherWeekAdapter
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
           viewModelFactory
        )[OverViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        Log.d("TAG", "onCreateView")
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "onViewCreated")
        val lat = 50.2997427
        val lon = 127.5023826
 //       val url = "lat=$lat&lon=$lon&exclude=minutely,alerts&appid=2e566c90702a14d162799e5be40e0a12&units=metric&lang=ru"
        viewModel.getWeather(lat, lon, WeatherApp.EXCLUDE, WeatherApp.APPID, WeatherApp.UNITS, WeatherApp.LANG)
        setupRecyclerView()
        initData()
        binding.loadWeatherButton.setOnClickListener {
//            val lat = 50.2997427
//            val lon = 127.5023826
          //  val url = "lat=$lat&lon=$lon&exclude=minutely,alerts&appid=2e566c90702a14d162799e5be40e0a12&units=metric&lang=ru"
            viewModel.getWeather(lat, lon, WeatherApp.EXCLUDE, WeatherApp.APPID, WeatherApp.UNITS, WeatherApp.LANG)
            initData()
        }
    }

    private fun initData() {
//        viewModel.status.observe(viewLifecycleOwner) {
//            println(it)
//        }
        Log.d("TAG", "startInitData")
        viewModel.currentWeatherEntity.observe(viewLifecycleOwner) {
            it?.let {
                val format = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
                val currentTime = System.currentTimeMillis()
                val currentTimeString = format.format(currentTime).toString() + "\n"+it.dt
                val feelLikeTemp = "Ощущается как " + it.feelsLike
                with(binding) {
                    bindImage(ivWeatherIcon, it.icon)
                    tvDescription.text = it.description
                    tvCurrentTemp.text = it.temp
                    tvCurrentTime.text = currentTimeString
                    tvFeelsLikeTemp.text = feelLikeTemp
                }
            }
        }
        viewModel.weekWeatherEntity.observe(viewLifecycleOwner) {
            weatherAdapter.submitList(it)
        }
        viewModel.hourlyWeatherEntity.observe(viewLifecycleOwner) {
            hourlyWeatherAdapter.submitList(it)
            //            for(i in it){
//                Log.d("TAG", i.dt)
//            }
//            Log.d("TAG", it.size.toString())
        }
        binding.clCurrentWeather.setOnClickListener {
            with(binding.rvHourlyWeather){
                visibility = if(visibility == View.GONE) View.VISIBLE
                else View.GONE
            }
        }
    }

    private fun bindImage(imgView: ImageView, icon: String) {
        val imgUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
        imgUrl.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
    }

    private fun setupRecyclerView() {
        Log.d("TAG", "setupRecyclerView")
        with(binding.rvFurtherWeek) {
            layoutManager = LinearLayoutManager(activity)
            weatherAdapter = WeatherWeekAdapter()
            adapter = weatherAdapter
        }
        with(binding.rvHourlyWeather) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            hourlyWeatherAdapter = HourlyWeatherAdapter()
            adapter = hourlyWeatherAdapter
        }
    }
}
