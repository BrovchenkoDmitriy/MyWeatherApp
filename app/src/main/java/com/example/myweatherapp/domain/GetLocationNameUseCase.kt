package com.example.myweatherapp.domain

import javax.inject.Inject

class GetLocationNameUseCase @Inject constructor(private val repository: SearchLocationNameRepository) {
    suspend operator fun invoke(lat: Double, lon: Double) = repository.getLocationName(lat,lon)
}