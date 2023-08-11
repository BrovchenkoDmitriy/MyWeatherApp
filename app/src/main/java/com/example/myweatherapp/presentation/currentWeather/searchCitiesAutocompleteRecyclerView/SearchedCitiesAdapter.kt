package com.example.myweatherapp.presentation.currentWeather.searchCitiesAutocompleteRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myweatherapp.databinding.SearchedCityItemBinding
import com.example.myweatherapp.domain.SearchedCities

class SearchedCitiesAdapter :
    ListAdapter<SearchedCities, SearchedCitiesViewHolder>(SearchedCitiesDiffCallBack()) {

    var onItemClickListener: ((SearchedCities) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedCitiesViewHolder {
        val binding = SearchedCityItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchedCitiesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchedCitiesViewHolder, position: Int) {
        val searchedCity = getItem(position)
        with(holder.binding) {
            tvCityName.text = searchedCity.city
            tvRegionDescription.text = searchedCity.unrestrictedValue
            root.setOnClickListener {
                onItemClickListener?.invoke(searchedCity)
            }
        }
    }
}