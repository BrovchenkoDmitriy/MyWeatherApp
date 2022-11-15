package com.example.myweatherapp.presentation.weekForecast

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.WeatherApp
import com.example.myweatherapp.databinding.FragmentWeekForecastBinding
import com.example.myweatherapp.presentation.ViewModelFactory
import com.example.myweatherapp.presentation.weekForecast.weekForecastRecyclerView.WeatherWeekAdapter
import javax.inject.Inject


class WeekForecastFragment : Fragment() {

    private var _binding: FragmentWeekForecastBinding? = null
    private val binding: FragmentWeekForecastBinding
        get() = _binding ?: throw RuntimeException("FragmentWeekForecastBinding is null")

    private lateinit var weatherAdapter: WeatherWeekAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[WeekForecastViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekForecastBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getWeather()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.weekWeatherDto.observe(viewLifecycleOwner){
            weatherAdapter.submitList(it)
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