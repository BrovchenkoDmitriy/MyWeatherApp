package com.example.myweatherapp.presentation.googleMap

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.BuildConfig
import com.example.myweatherapp.R
import com.example.myweatherapp.WeatherApp
import com.example.myweatherapp.databinding.FragmentMapBinding
import com.example.myweatherapp.presentation.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
        mapViewModel.currentWeatherDto.observe(viewLifecycleOwner) {
            googleMap.clear()
            val location = LatLng(it.lat, it.lon)
            val startMarker = googleMap.addMarker(MarkerOptions().position(location)) as Marker
            startMarker.title = it.temp
            startMarker.snippet = it.description
//              googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7f)) //без анимации
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 7f),
                object : CancelableCallback {
                    override fun onFinish() {
                        startMarker.showInfoWindow()
                    }

                    override fun onCancel() {
                        Log.d("SEARCH_LOCATION_NAME", googleMap.cameraPosition.target.toString())
                        if (startMarker.isDraggable) startMarker.showInfoWindow()
                    }
                })
        }

        googleMap.setOnMapLongClickListener {
            val lat = it.latitude
            val lon = it.longitude
            googleMap.addMarker(MarkerOptions().position(LatLng(lat, lon)))
        }

        googleMap.setOnMarkerClickListener {
            // googleMap.clear()
            onMarkerClick(it)
        }
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        loadData(marker.position.latitude, marker.position.longitude)
        mapViewModel.state.observe(viewLifecycleOwner) {
            marker.hideInfoWindow()
            when (it) {
                is Loading -> {
                    marker.title = "Загрузка данных"
                    marker.snippet = "пожалуйста подождите"
                    marker.showInfoWindow()
                }

                is Success -> {
                    marker.title = it.currentWeatherEntity.temp
                    marker.snippet = it.currentWeatherEntity.description
                    marker.showInfoWindow()
                }

                is Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
            }
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