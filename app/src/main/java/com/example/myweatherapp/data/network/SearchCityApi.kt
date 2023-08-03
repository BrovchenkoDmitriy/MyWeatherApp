package com.example.myweatherapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface SearchCityApi {
    @GET("suggest")
    suspend fun getSearchedCitiesList(
        @Query("q") query: String,
        @Query("types") types:String,
        @Query("session_token") sessionToken:String,
        @Query("access_token") accessToken:String
    ):SearchedCitiesDto
}

class SearchCityApiImpl @Inject constructor(
    private val searchCityApi: SearchCityApi
): SearchCityApi {
    override suspend fun getSearchedCitiesList(
        query: String,
        types: String,
        sessionToken: String,
        accessToken: String
    ): SearchedCitiesDto {
        return searchCityApi.getSearchedCitiesList(query,types,sessionToken,accessToken)
    }

}