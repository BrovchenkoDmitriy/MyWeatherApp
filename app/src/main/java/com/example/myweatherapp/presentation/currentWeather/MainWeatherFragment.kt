package com.example.myweatherapp.presentation.currentWeather

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
import com.example.myweatherapp.R
import com.example.myweatherapp.WeatherApp
import com.example.myweatherapp.databinding.FragmentMainWeatherBinding
import com.example.myweatherapp.presentation.ViewModelFactory
import com.example.myweatherapp.presentation.currentWeather.hourlyForecastRecyclerView.HourlyWeatherAdapter
import com.example.myweatherapp.presentation.weekForecast.weekForecastRecyclerView.WeatherWeekAdapter
import java.util.*
import javax.inject.Inject

class MainWeatherFragment : Fragment() {

    private var _binding: FragmentMainWeatherBinding? = null
    private val binding: FragmentMainWeatherBinding
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
        )[MainWeatherViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainWeatherBinding.inflate(layoutInflater, container, false)
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
        viewModel.getWeather(
            lat,
            lon,
            WeatherApp.EXCLUDE,
            WeatherApp.APPID,
            WeatherApp.UNITS,
            WeatherApp.LANG
        )
        setupRecyclerView()
        initData()
        binding.loadWeatherButton.setOnClickListener {
            viewModel.getWeather(
                lat,
                lon,
                WeatherApp.EXCLUDE,
                WeatherApp.APPID,
                WeatherApp.UNITS,
                WeatherApp.LANG
            )
            initData()
        }
    }

    private fun initData() {
        Log.d("TAG", "startInitData")
        binding.clCurrentWeather.visibility = View.GONE
        binding.rvFurtherWeek.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        viewModel.currentWeatherEntity.observe(viewLifecycleOwner) {
            it?.let {
                val format = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
                val currentTime = System.currentTimeMillis()
                val currentTimeString = format.format(currentTime).toString() + "\n" + it.dt
                val feelLikeTemp = "Ощущается как " + it.feelsLike
                binding.progressBar.visibility = View.GONE
                binding.clCurrentWeather.visibility = View.VISIBLE
                binding.rvFurtherWeek.visibility = View.VISIBLE

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
        }
        binding.clCurrentWeather.setOnClickListener {
            with(binding.rvHourlyWeather) {
                visibility = if (visibility == View.GONE) View.VISIBLE
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
