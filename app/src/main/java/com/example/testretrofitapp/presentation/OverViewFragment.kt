package com.example.testretrofitapp.presentation

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
import com.example.testretrofitapp.databinding.FragmentOverviewBinding
import java.util.*


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding: FragmentOverviewBinding
        get() = _binding ?: throw RuntimeException("FragmentOverviewBinding is null")

    private lateinit var weatherAdapter: WeatherWeekAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[OverViewModel::class.java]
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
        setupRecyclerView()
        initData()
        binding.loadWeatherButton.setOnClickListener {
            viewModel.getWeather()
            initData()
        }
    }

    private val format = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())

    private fun initData() {
//        viewModel.status.observe(viewLifecycleOwner) {
//            println(it)
//        }
        Log.d("TAG", "startInitData")
        viewModel.weatherDto.observe(viewLifecycleOwner) {
            it?.let {

                val currentTime = System.currentTimeMillis()
                val currentTimeString = format.format(currentTime).toString() + it.currentEntity.dt
                val feelLikeTemp = "Ощущается как " + it.currentEntity.feelsLike

                with(binding) {
                    bindImage(ivWeatherIcon, it.currentEntity.weather.icon)
                    tvDescription.text = it.currentEntity.weather.description
                    tvCurrentTemp.text = it.currentEntity.temp
                    tvCurrentTime.text = currentTimeString
                    tvFeelsLikeTemp.text = feelLikeTemp
                    weatherAdapter.weatherList = it.dailyEntity
                }
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
    }
}
