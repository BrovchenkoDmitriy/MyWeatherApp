package com.example.myweatherapp.domain

interface SearchCityAutocompleteRepository {

    suspend fun getSearchedCities( query: String,
//                                   types: String,
//                                   sessionToken: String,
//                                   accessToken: String
    ): List<SearchedCities>
}