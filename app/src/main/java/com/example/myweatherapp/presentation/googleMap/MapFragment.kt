package com.example.myweatherapp.presentation.googleMap

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.R
import com.example.myweatherapp.WeatherApp
import com.example.myweatherapp.databinding.FragmentMapBinding
import com.example.myweatherapp.presentation.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class MapFragment : Fragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private val mapViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[MapViewModel::class.java]
    }

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentMapBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.uploadCurrentWeather()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //  startMarker?.tag = 0
        lifecycleScope.launch {
            mapViewModel.currentWeatherDto.observe(viewLifecycleOwner) {
                val location = LatLng(it.lat, it.lon)
                val startMarker = googleMap.addMarker(MarkerOptions().position(location)) as Marker
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7f))
                startMarker.title = it.temp
                startMarker.snippet = it.description
                startMarker.showInfoWindow()
            }
        }

        googleMap.setOnMapLongClickListener {
            val lat = it.latitude
            val lon = it.longitude
            googleMap.addMarker(MarkerOptions().position(LatLng(lat, lon)))
        }

        googleMap.setOnMarkerClickListener {
            onMarkerClick(it)
        }
    }

    private fun onMarkerClick(marker: Marker): Boolean {

        val jobLoad = lifecycleScope.launch {
            val firstData = mapViewModel.getCurrentWeather()
            loadData(marker.position.latitude, marker.position.longitude)
            marker.title = "Загрузка данных"
            marker.snippet = "пожалуйста подождите"
            marker.showInfoWindow()

            //delay while loading new data
            delay(1000)
            var secondData = mapViewModel.getCurrentWeather()
            while (firstData == secondData) {
                secondData = mapViewModel.getCurrentWeather()
                delay(500)
            }
        }

        lifecycleScope.launch {
            jobLoad.join()
            marker.hideInfoWindow()
            val it = mapViewModel.getCurrentWeather()
            marker.title = it.temp
            marker.snippet = it.description
            marker.showInfoWindow()
        }
        return true // if true - cancel standard actions and complete code of onMarkerClick()
    }

    private fun loadData(lat: Double, lon: Double) {
        mapViewModel.getWeather(
            lat,
            lon,
            WeatherApp.EXCLUDE,
            BuildConfig.OPEN_WEATHER_API_KEY,
            WeatherApp.UNITS,
            WeatherApp.LANG
        )
    }

    companion object {
        const val LOCATION_PERMISSION_RC = 100
    }
}