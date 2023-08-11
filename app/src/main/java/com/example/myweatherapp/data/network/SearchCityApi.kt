package com.example.myweatherapp.data.network

import com.example.myweatherapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Inject

interface SearchCityApi {
    @Headers(
        "Content-type: application/json",
        "Accept: application/json",
        "Authorization: Token ${BuildConfig.DADATA_API_KEY}"
    )
    @GET("address")
    suspend fun getSearchedCitiesList(
        @Query("query") query: String
    ):SearchedCitiesDto
}

class SearchCityApiImpl @Inject constructor(
    private val searchCityApi: SearchCityApi
) : SearchCityApi {
    override suspend fun getSearchedCitiesList(
        query: String
    ): SearchedCitiesDto {
        return searchCityApi.getSearchedCitiesList(query)
    }

}