package com.example.wapp.data.remote.api

import com.example.wapp.data.remote.model.GeocodeResponse
import com.example.wapp.data.remote.model.MapboxSearchResponse
import com.example.wapp.data.remote.model.SearchResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query
interface MapboxSearchService {
    @GET("search/searchbox/v1/suggest")
    suspend fun getSuggestedResults(
        @Query("q") query: String,
        @Query("access_token") accessToken: String,
        @Query("session_token") sessionToken: String, // Optional: Group requests for billing (UUIDv4 recommended)
        @Query("types") types: String = "poi", // Filter ke kategori POI (point of interest)
        @Query("limit") limit: Int = 5, // Batas jumlah hasil
        @Query("country") country: String = "ID" // Batasi hasil pencarian ke Indonesia (ID)
    ): SearchResponse


    @GET("search/searchbox/v1/retrieve/{id}")
    suspend fun getFeatureDetails(
        @Path("id") id: String,
        @Query("session_token") sessionToken: String,
        @Query("access_token") accessToken: String
    ): MapboxSearchResponse

    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") accessToken: String
    ): Response<GeocodeResponse>

}