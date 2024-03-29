package com.example.myweatherapp.presentation.currentWeather

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
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
import com.google.android.gms.maps.model.LatLng
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("SEARCH_CITIES", "onCreate $this")
        if (arguments != null) {
            if (savedInstanceState == null) {
                val lat1 = requireArguments().getString("Lat", "0.0")
                val lon1 = requireArguments().getString("Lon", "0.0")
                getNewWeather(lat1.toDouble(), lon1.toDouble())
            }
        }
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
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { currentWeatherState ->
            when (currentWeatherState) {
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Error -> {
                    Toast.makeText(requireContext(), currentWeatherState.error, Toast.LENGTH_SHORT).show()
                    viewModel.changeStateStatus()
                }

                is Success -> {
                    viewModel.getWeather()
                    initData()
                    crossFadeAnimation()
                }
            }
        }
        setupRecyclerView()

        binding.etSearchCity.addTextChangedListener {
            it?.let {
                lifecycleScope.launchWhenResumed {
                    viewModel.searchCityState.emit(it.toString())
                }
            }
        }
    }

    private fun getNewWeather(lat: Double, lon: Double) {
        viewModel.getNewWeather(
            lat,
            lon,
            WeatherApp.EXCLUDE,
            BuildConfig.OPEN_WEATHER_API_KEY,
            WeatherApp.UNITS,
            WeatherApp.LANG
        )
    }

    private fun initData() {
        Log.d("TAG", "startInitData")
        viewModel.currentWeatherEntity.observe(viewLifecycleOwner) {
            val format = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
            val currentTime = System.currentTimeMillis()
            val currentTimeString =
                format.format(currentTime).toString() + "\n" + "данные от " + it.dt
            val feelLikeTemp = "Ощущается как " + it.feelsLike

            with(binding) {
                bindImage(ivWeatherIcon, it.icon)
                tvDescription.text = it.description
                tvCurrentTemp.text = it.temp
                tvCurrentTime.text = currentTimeString
                tvFeelsLikeTemp.text = feelLikeTemp
                binding.loadWeatherButton.text = it.locationName
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
        binding.progressBar.visibility = View.GONE
    }

    private fun bindImage(imgView: ImageView, icon: String) {
        val imgUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
        imgUrl.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
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
                when(it.postalCode){ //API по этим городам не предоставляет координаты
                    "299700" -> getNewWeather(SEVASTOPOL.latitude, SEVASTOPOL.longitude)
                    "101000" -> getNewWeather(MOSCOW.latitude, MOSCOW.longitude)
                    "190000" -> getNewWeather(S_PETERSBURG.latitude, S_PETERSBURG.longitude)
                    "468320" -> getNewWeather(BAIKONUR.latitude, BAIKONUR.longitude)
                    else ->  getNewWeather(it.geoLat, it.geoLon)

                }
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
    companion object{
        private val MOSCOW = LatLng(55.7543,37.6166)
        private val S_PETERSBURG = LatLng(59.9310,30.3609)
        private val SEVASTOPOL = LatLng(44.608,33.5213)
        private val BAIKONUR = LatLng(45.6322,63.3209)
    }
}
