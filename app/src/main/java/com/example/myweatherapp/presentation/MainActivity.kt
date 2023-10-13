package com.example.myweatherapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.presentation.googleMap.MapFragment
import com.google.android.gms.common.util.concurrent.HandlerExecutor
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: FragmentManager
    private lateinit var navController: NavController

    private var locationPermissionsIsGranted by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TestRetrofitApp)
        manager = supportFragmentManager
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_current_weather,
                R.id.navigation_week_forecast,
                R.id.navigation_map_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (savedInstanceState == null) {
            getCurrentLocation()
        }
    }

    private fun startFirstDestination(latLng: LatLng) {
        //now need that start destination will receive arguments
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)
        val latitude = NavArgument.Builder().setDefaultValue(latLng.latitude.toString()).build()
        val longitude = NavArgument.Builder().setDefaultValue(latLng.longitude.toString()).build()
        graph.addArgument("Lat", latitude)
        graph.addArgument("Lon", longitude)
        navController.graph = graph
        binding.startLogoImage.visibility = View.GONE
    }


    private fun getCurrentLocation() {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionsIsGranted = false
            Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT)
                .show()
            requestLocationPermission()
        } else {
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val executor = if (VERSION.SDK_INT >= 30) {
                mainExecutor
            } else {
                HandlerExecutor(mainLooper)
            }
            val isLocationEnabled = if (VERSION.SDK_INT >= 28) {
                locationManager.isLocationEnabled
            } else {
                LocationManagerCompat.isLocationEnabled(locationManager)
            }
            locationPermissionsIsGranted = true
            val context = applicationContext
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val bestLocationProvider =
                if (capabilities == null) {
                    LocationManager.GPS_PROVIDER
                } else {
                    if ((capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    ) {
                        LocationManager.NETWORK_PROVIDER
                    } else {
                        LocationManager.GPS_PROVIDER
                    }
                }

            if (!isLocationEnabled) {
                // Location disabled
                Toast.makeText(
                    this@MainActivity,
                    "location disabled. The screen will display information about your last saved location.",
                    Toast.LENGTH_LONG
                ).show()
                // если геолокация отключена, то любой запрос на локацию вернёт null
                // как вариант можно брать координаты из БД (например "домашний город")
                val location = locationManager.getLastKnownLocation(bestLocationProvider)
                startFirstDestination(LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0))
            } else {
                if (VERSION.SDK_INT >= 30) {
                    locationManager.getCurrentLocation(
                        bestLocationProvider,
                        null,
                        executor
                    )
                    {
                        startFirstDestination(LatLng(it.latitude, it.longitude))
                    }
                    return
                } else {
                    LocationManagerCompat.getCurrentLocation(
                        locationManager,
                        bestLocationProvider,
                        null,
                        executor
                    )
                    {
                        startFirstDestination(LatLng(it.latitude, it.longitude))
                    }
                    return
                }
            }
        }
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            MapFragment.LOCATION_PERMISSION_RC
        )
    }
}
