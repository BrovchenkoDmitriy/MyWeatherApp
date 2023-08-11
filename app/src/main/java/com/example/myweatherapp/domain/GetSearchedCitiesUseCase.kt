package com.example.myweatherapp.domain

import javax.inject.Inject

class GetSearchedCitiesUseCase @Inject constructor(
    private val repository: SearchCityAutocompleteRepository
) {
    suspend operator fun invoke(
        query: String
    ) = repository.getSearchedCities(query)
}