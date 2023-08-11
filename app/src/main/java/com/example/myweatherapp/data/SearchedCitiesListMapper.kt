package com.example.myweatherapp.data

import com.example.myweatherapp.data.network.Suggestion
import com.example.myweatherapp.domain.SearchedCities
import javax.inject.Inject

class SearchedCitiesListMapper @Inject constructor() {

    private fun mapSearchedCitiesDtoToSearchedCities(suggestion: Suggestion): SearchedCities =
        SearchedCities(
            value = suggestion.value,
            unrestrictedValue = suggestion.unrestrictedValue,

            postalCode = suggestion.data.postalCode,
            country = suggestion.data.country,
            countryISOCode = suggestion.data.countryISOCode?:"",
            federalDistrict = suggestion.data.federalDistrict?:"",
            regionISOCode = suggestion.data.regionISOCode?:"",
            regionWithType = suggestion.data.regionWithType,
            regionType = suggestion.data.regionType,
            regionTypeFull = suggestion.data.regionTypeFull,
            region = suggestion.data.region,
            cityWithType = suggestion.data.cityWithType?:"",
            city = suggestion.data.city?:"",
            cityArea = suggestion.data.cityArea,
            streetTypeFull = suggestion.data.streetTypeFull,
            street = suggestion.data.street,
            geoLat = suggestion.data.geoLat?:"",
            geoLon = suggestion.data.geoLon?:""
        )

    fun mapDtoListToEntityList(list: List<Suggestion>): List<SearchedCities> = list.map {
        mapSearchedCitiesDtoToSearchedCities(it)
    }.filter { it.street==null && it.geoLat.isNotEmpty() }
}