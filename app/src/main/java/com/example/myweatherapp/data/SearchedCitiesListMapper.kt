package com.example.myweatherapp.data

import com.example.myweatherapp.data.network.Suggestion
import com.example.myweatherapp.domain.SearchedCities
import javax.inject.Inject

class SearchedCitiesListMapper @Inject constructor() {

    fun mapSearchedCitiesDtoToSearchedCities(suggestion: Suggestion): SearchedCities =
        SearchedCities(
            name = suggestion.name,
            namePreferred = suggestion.namePreferred,
            placeFormatted = suggestion.placeFormatted
        )

    fun mapDtoListToEntityList(list: List<Suggestion>): List<SearchedCities> = list.map {
        mapSearchedCitiesDtoToSearchedCities(it)
    }
}