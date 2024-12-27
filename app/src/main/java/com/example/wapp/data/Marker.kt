package com.example.wapp.data

import com.google.firebase.firestore.GeoPoint
import com.mapbox.geojson.Point



data class Markers(
    val id: String? = null,
    val markerId:String? =null,
    val locationName : String,
    val streetName: String,
    val type: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val categoryId: String,
    val addedBy: String,
    val createdAt: String,
    val updatedAt: String
)

