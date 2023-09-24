package com.example.myweatherapp.data

import android.util.Log
import com.example.myweatherapp.data.network.SearchLocationNameApi
import com.example.myweatherapp.domain.SearchLocationNameRepository
import javax.inject.Inject

class SearchLocationNameRepositoryImpl @Inject constructor(
    private val searchLocationNameApi: SearchLocationNameApi,
    private val mapper: SearchedCitiesListMapper
) : SearchLocationNameRepository {
    override suspend fun getLocationName(
        lat: Double,
        lon: Double
    ): String {
        return try {
            val searchedLocationNameDto =
                searchLocationNameApi.getLocationName(lat, lon, DETAIL_RESPONSE, FORMAT_OF_RESPONSE)
            searchedLocationNameDto.displayName
        } catch (e: Exception) {
            Log.d("SEARCH_LOCATION_NAME", "RepoImpl error:  " + e.stackTraceToString())
            EMPTY_RESPONSE
        }
    }

    companion object {
        private val DETAIL_RESPONSE = DetailOfResponse.TOWN.value
        private const val FORMAT_OF_RESPONSE = "jsonv2"
        private const val EMPTY_RESPONSE = ""
    }

    enum class DetailOfResponse(val value: String) {
        COUNTRY("3"),
        STATE("5"),
        COUNTY("8"),
        CITY("10"),
        TOWN("12")
    }
}