package com.example.myweatherapp.domain

interface SearchCityAutocompleteRepository {

    suspend fun getSearchedCities( query: String,
    ): List<SearchedCities>
}