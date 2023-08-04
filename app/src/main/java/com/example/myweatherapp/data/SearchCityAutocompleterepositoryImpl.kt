package com.example.myweatherapp.data

import android.util.Log
import com.example.myweatherapp.data.network.SearchCityApi
import com.example.myweatherapp.domain.SearchCityAutocompleteRepository
import com.example.myweatherapp.domain.SearchedCities
import javax.inject.Inject

class SearchCityAutocompleteRepositoryImpl @Inject constructor(
    private val searchCityApi: SearchCityApi,
    private val mapper: SearchedCitiesListMapper
): SearchCityAutocompleteRepository {
    override suspend fun getSearchedCities(
        query: String,
        types: String,
        sessionToken: String,
        accessToken: String
    ): List<SearchedCities>
    {
        return try {
            val searchedCitiesDto = searchCityApi.getSearchedCitiesList(query,types,sessionToken,accessToken)
            mapper.mapDtoListToEntityList(searchedCitiesDto.suggestions).toList()
        } catch (e: Exception) {
            Log.d("TAG", e.stackTraceToString())
            listOf()
        }
    }

}