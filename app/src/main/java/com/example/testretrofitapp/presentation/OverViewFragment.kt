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
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testretrofitapp.R
import com.example.testretrofitapp.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {


    private var _binding: FragmentOverviewBinding? = null
    private val binding: FragmentOverviewBinding
        get() = _binding ?: throw RuntimeException("FragmentOverviewBinding is null")

    //private lateinit var viewModel: OverViewModel
    private lateinit var weatherAdapter: WeatherWeekAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager


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

//        binding.rvFurtherWeek.layoutManager = LinearLayoutManager(activity)
//            binding.rvFurtherWeek.adapter = WeatherWeekAdapter()
        Log.d("TAG", "onCreateView")

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this, )[OverViewModel::class.java]
        Log.d("TAG", "onViewCreated")
        setupRecyclerView()
        initData()

        binding.loadWeatherButton.setOnClickListener {
            viewModel.getWeather()
            initData()
        }
    }


    private val format = SimpleDateFormat("dd MMMM, HH:mm")

    private fun initData() {
//        viewModel.status.observe(viewLifecycleOwner) {
//            println(it)
//        }
        Log.d("TAG", "startInitData")
        viewModel.weatherDto.observe(viewLifecycleOwner) {
            Log.d("TAG", "in obesrver of Weather live data")
            it?.let {
                Log.d("TAG", "in obesrver of Weather live data")
                val icon = it.currentDto.weather[0].icon
                val imageIcon = "http://openweathermap.org/img/wn/$icon@2x.png"
                bindImage(binding.imWeatherIcon, imageIcon)

                val currentTime = System.currentTimeMillis()
                val currentTimeString = format.format(currentTime).toString()

                val currentTemp = it.currentDto.temp.substring(0, 2) + "\u00B0C"
                val feelLikeTemp = "Ощущается как "+it.currentDto.feelsLike.substring(0, 2) + "\u00B0C"
                //with(binding) {
                binding.tvDescription.text = it.currentDto.weather[0].description.capitalize()
                binding.tvCurrentTemp.text = currentTemp
                binding.tvCurrentTime.text = currentTimeString
                binding.tvFeelsLikeTemp.text = feelLikeTemp
                weatherAdapter.weatherList = it.dailyDto
                Log.d(
                    "TAG",
                    "size of adapter.weatherList " + weatherAdapter.weatherList.size.toString()
                )
                // }

            }
        }

    }

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
    }

    fun setupRecyclerView() {
        Log.d("TAG", "setupRecyclerView")
        with(binding.rvFurtherWeek) {
            layoutManager = LinearLayoutManager(activity)
            weatherAdapter = WeatherWeekAdapter()
            adapter = weatherAdapter
        }
    }
}
