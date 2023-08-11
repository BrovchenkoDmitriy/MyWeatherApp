package com.example.myweatherapp.data.network

import com.example.myweatherapp.BuildConfig
import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Inject

interface SearchCityApi {
    @Headers(
        "Content-type: application/json",
        "Accept: application/json",
        "Authorization: Token ${BuildConfig.DADATA_API_KEY}"
    )
    @POST("address")
    suspend fun getSearchedCitiesList(
        @Body query: MyRequestBody
    ):SearchedCitiesDto
}

class SearchCityApiImpl @Inject constructor(
    private val searchCityApi: SearchCityApi
) : SearchCityApi {
    override suspend fun getSearchedCitiesList(
        query: MyRequestBody
    ): SearchedCitiesDto {
        return searchCityApi.getSearchedCitiesList(query)
    }

}

data class MyRequestBody(
    val query: String,
    val language: String,
    @Json(name = "from_bound")
    val fromBound: Bound,
    @Json(name = "to_bound")
    val toBound: Bound,
    val locations: List<Location>
)

data class Bound(
    val value: String
)

data class Location(
    val country: String
)