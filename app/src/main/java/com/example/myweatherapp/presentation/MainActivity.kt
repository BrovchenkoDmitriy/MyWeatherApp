package com.example.myweatherapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.presentation.googleMap.MapFragment
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
                R.id.navigation_current_weather, R.id.navigation_week_forecast, R.id.navigation_map_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if( savedInstanceState == null){
            val latLng = getCurrentLocation()
            val arg = Bundle().apply {
                putString("Lat", latLng?.latitude.toString())
                putString("Lon", latLng?.longitude.toString())
            }
            navController.navigate(R.id.navigation_current_weather, arg)
        }

    }

    private fun getCurrentLocation(): LatLng? {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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
            Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT)
                .show()
            requestLocationPermission()
            null
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
