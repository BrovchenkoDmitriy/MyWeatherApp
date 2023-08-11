package com.example.myweatherapp.presentation.currentWeather.searchCitiesAutocompleteRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherapp.domain.SearchedCities

class SearchedCitiesDiffCallBack : DiffUtil.ItemCallback<SearchedCities> (){
    override fun areItemsTheSame(oldItem: SearchedCities, newItem: SearchedCities): Boolean {
       return oldItem.unrestrictedValue == newItem.unrestrictedValue
    }

    override fun areContentsTheSame(oldItem: SearchedCities, newItem: SearchedCities): Boolean {
        return oldItem == newItem
    }
}