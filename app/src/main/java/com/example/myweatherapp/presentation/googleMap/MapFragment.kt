package com.example.myweatherapp.presentation.googleMap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import kotlin.properties.Delegates


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

    private lateinit var mMap: GoogleMap

    private lateinit var currentLocation: LatLng

    private var locationPermissionsIsGranted by Delegates.notNull<Boolean>()

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding?: throw RuntimeException("FragmentMapBinding is null")

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
        requestLocationPermission()
        currentLocation = getCurrentLocation()
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.uploadCurrentWeather()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        if (locationPermissionsIsGranted) {
            val startMarker = mMap.addMarker(MarkerOptions().position(currentLocation)) as Marker
            Log.d("TAGA", "create startMarker")
            //  startMarker?.tag = 0
            lifecycleScope.launch {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 7f))
                mapViewModel.currentWeatherDto.observe(viewLifecycleOwner) {
                    startMarker.title = it.temp
                    startMarker.snippet = it.description
                    Log.d("TAGA", it.temp)
                }
                delay(500)
                Log.d("TAGA", "after observe")
                startMarker.showInfoWindow()
            }
        }

        mMap.setOnMapLongClickListener {
            val lat = it.latitude
            val lon = it.longitude
            mMap.addMarker(MarkerOptions().position(LatLng(lat, lon)))
        }

        mMap.setOnMarkerClickListener {
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

    private fun getCurrentLocation(): LatLng {
        val context = requireContext()
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionsIsGranted = true
            val location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            val lat = location?.latitude ?: 0.0
            val lon = location?.longitude ?: 0.0

            LatLng(lat, lon)

        } else {
            locationPermissionsIsGranted = false
            Toast.makeText(requireContext(), "Location permission denied!", Toast.LENGTH_SHORT)
                .show()
            requestLocationPermission()
             LatLng(0.0, 0.0)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_RC
        )
    }

    companion object {
        const val LOCATION_PERMISSION_RC = 100
    }
}