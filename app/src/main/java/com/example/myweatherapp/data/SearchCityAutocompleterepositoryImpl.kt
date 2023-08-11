package com.example.myweatherapp.data

import android.util.Log
import com.example.myweatherapp.data.network.Location
import com.example.myweatherapp.data.network.Bound
import com.example.myweatherapp.data.network.MyRequestBody
import com.example.myweatherapp.data.network.SearchCityApi
import com.example.myweatherapp.domain.SearchCityAutocompleteRepository
import com.example.myweatherapp.domain.SearchedCities
import javax.inject.Inject

class SearchCityAutocompleteRepositoryImpl @Inject constructor(
    private val searchCityApi: SearchCityApi,
    private val mapper: SearchedCitiesListMapper
) : SearchCityAutocompleteRepository {
    override suspend fun getSearchedCities(
        query: String,
    ): List<SearchedCities> {
        return try {
            val searchedCitiesDto = searchCityApi.getSearchedCitiesList(
                MyRequestBody(
                    query,
                    "ru",
                    Bound("city"),
                    Bound("settlement"),
                    listOf(Location("*"))
                )
            )
            Log.d("SEARCH_CITIES", "RepoImpl:  " + searchedCitiesDto.suggestions.toString())
            mapper.mapDtoListToEntityList(searchedCitiesDto.suggestions).toList()
        } catch (e: Exception) {
            Log.d("SEARCH_CITIES", "RepoImpl:  "+ e.stackTraceToString())
            listOf()
        }
    }

}