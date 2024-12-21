package com.example.wapp.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://devandcreative.com/"

    // Instance Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val uploadService: UploadService by lazy {
        retrofit.create(UploadService::class.java)
    }
}

object MapboxRetrofitClient {
    private const val BASE_URL = "https://api.mapbox.com/"

    // Instance Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val geocodingService: MapboxSearchService by lazy {
        retrofit.create(MapboxSearchService::class.java)
    }

}