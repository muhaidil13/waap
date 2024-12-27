package com.example.wapp.data.remote.model


data class SearchResponse(
    val suggestions: List<Suggestion>,
    val attribution: String
)

data class Suggestion(
    val name: String,
    val mapbox_id: String,
    val feature_type: String,
    val address: String?,
    val full_address: String?,
    val place_formatted: String,
    val context: Context
)

data class MapboxSearchResponse(
    val type: String?,
    val features: List<Feature>?,
    val attribution: String?
)
