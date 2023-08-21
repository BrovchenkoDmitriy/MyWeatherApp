package com.example.myweatherapp.presentation.currentWeather

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import coil.load
import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.R
import com.example.myweatherapp.WeatherApp
import com.example.myweatherapp.databinding.FragmentMainWeatherBinding
import com.example.myweatherapp.presentation.ViewModelFactory
import com.example.myweatherapp.presentation.currentWeather.hourlyForecastRecyclerView.HourlyWeatherAdapter
import com.example.myweatherapp.presentation.currentWeather.searchCitiesAutocompleteRecyclerView.SearchedCitiesAdapter
import kotlinx.coroutines.delay
import java.util.Locale
import javax.inject.Inject

class MainWeatherFragment : Fragment() {

    private var _binding: FragmentMainWeatherBinding? = null
    private val binding: FragmentMainWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentOverviewBinding is null")

    private lateinit var searchedCitiesAdapter: SearchedCitiesAdapter
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
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy")
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "onViewCreated")
        val lat = 50.2997427
        val lon = 127.5023826
        viewModel.clearLiveData()
        getWeather(lat, lon)
        setupRecyclerView()
        initData()

        binding.etSearchCity.addTextChangedListener {
            it?.let {
                lifecycleScope.launchWhenResumed {
                    delay(1000)
                    searchCities(it.toString())
                }
            }
        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        viewModel.getWeather(
            lat,
            lon,
            WeatherApp.EXCLUDE,
            BuildConfig.OPEN_WEATHER_API_KEY,
            WeatherApp.UNITS,
            WeatherApp.LANG
        )
    }

    private fun searchCities(query: String) {
        viewModel.searchCities(query)
    }

    private fun initData() {
        Log.d("TAG", "startInitData")
        viewModel.currentWeatherEntity.observe(viewLifecycleOwner) {
            it?.let {
                val format = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
                val currentTime = System.currentTimeMillis()
                val currentTimeString = format.format(currentTime).toString() + "\n" + it.dt
                val feelLikeTemp = "Ощущается как " + it.feelsLike

                with(binding) {
                    bindImage(ivWeatherIcon, it.icon)
                    tvDescription.text = it.description
                    tvCurrentTemp.text = it.temp
                    tvCurrentTime.text = currentTimeString
                    tvFeelsLikeTemp.text = feelLikeTemp
                }
                crossFadeAnimation()
            }
        }
        viewModel.searchedCities.observe(viewLifecycleOwner) {
            searchedCitiesAdapter.submitList(it)
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

    private fun crossFadeAnimation() {
        val shortAnimationDuration = resources.getInteger(android.R.integer.config_longAnimTime)
        binding.progressBar.visibility = View.GONE
        binding.clCurrentWeather.visibility = View.VISIBLE
//        binding.rvFurtherWeek.visibility = View.VISIBLE
        binding.clCurrentWeather.apply {
            // Set the content view to 0% opacity but visible, so that it is visible
            // (but fully transparent) during the animation.
            alpha = 0f
            visibility = View.VISIBLE

            // Animate the content view to 100% opacity, and clear any animation
            // listener set on the view.
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
        binding.rvSearchCity.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        binding.progressBar.animate()
            .alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.progressBar.visibility = View.GONE
                }
            })
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
        with(binding.rvSearchCity) {
            layoutManager = LinearLayoutManager(activity)
            searchedCitiesAdapter = SearchedCitiesAdapter()
            adapter = searchedCitiesAdapter
            searchedCitiesAdapter.onItemClickListener = {
                binding.etSearchCity.text.clear()
                binding.etSearchCity.clearFocus()
                binding.loadWeatherButton.text = it.value
                searchCities("")
                getWeather(it.geoLat, it.geoLon)
              // remove softKeyBoard after chose city
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(windowToken, 0)
            }
        }
        with(binding.rvHourlyWeather) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            hourlyWeatherAdapter = HourlyWeatherAdapter()
            adapter = hourlyWeatherAdapter
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvHourlyWeather)
    }

}
