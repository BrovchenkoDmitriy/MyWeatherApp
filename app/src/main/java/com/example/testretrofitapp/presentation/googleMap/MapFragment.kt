package com.example.testretrofitapp.presentation.googleMap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.testretrofitapp.R
import com.example.testretrofitapp.WeatherApp
import com.example.testretrofitapp.databinding.FragmentMapBinding
import com.example.testretrofitapp.presentation.ViewModelFactory
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

    private lateinit var mMap: GoogleMap

    private lateinit var currentLocation: LatLng


    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
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
        currentLocation = getCurrentLocation()
        //loadData(currentLocation.latitude,currentLocation.longitude)
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.getLiveData()
    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

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



//        val it = mapViewModel.getCurrentWeather()
//        startMarker.title = it.temp + " " + it.description



        mMap.setOnMapLongClickListener {
            val lat = it.latitude
            val lon = it.longitude
            mMap.addMarker(MarkerOptions().position(LatLng(lat,lon)))
        }

        mMap.setOnMarkerClickListener{
            onMarkerClick(it)
        }
    }

    private fun onMarkerClick(marker: Marker):Boolean{
       lifecycleScope.launch{
           loadData(marker.position.latitude,marker.position.longitude)
           marker.title = "Загрузка данных"
           marker.snippet="."
           marker.showInfoWindow()
           delay(1000)
           marker.hideInfoWindow()

           marker.title = "Загрузка данных"
           marker.snippet=".."
           marker.showInfoWindow()
           delay(1000)
           marker.hideInfoWindow()

           marker.title = "Загрузка данных"
           marker.snippet="..."
           marker.showInfoWindow()
           delay(1000)

           marker.hideInfoWindow()
           val it = mapViewModel.getCurrentWeather()
           //mapViewModel.currentWeatherDto.observe(viewLifecycleOwner){
               marker.title = it.temp
           marker.snippet = it.description
           //}
           marker.showInfoWindow()
        }
        return true
    }
    private fun loadData(lat: Double, lon: Double) {
        mapViewModel.getWeather(
            lat,
            lon,
            WeatherApp.EXCLUDE,
            WeatherApp.APPID,
            WeatherApp.UNITS,
            WeatherApp.LANG
        )
    }
    private fun getCurrentLocation(): LatLng {
        val context = requireContext()
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val location = if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return LatLng(0.0, 0.0)
        } else {
            locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        }

        val lat = location?.latitude
        val lon = location?.longitude

        currentLocation = LatLng(lat ?: 0.0, lon ?: 0.0)
        return currentLocation
    }
}