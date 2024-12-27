package com.example.wapp.data

data class NavigationHistory(
    val navId: String? = "",
    val userId: String,
    val destinationId: String,
    val startStreetName: String,
    val totalJarak:  String,
    val totalWaktu: String,
    val startTime: String,
    val createdAt: String,
    val endTime: String? = "",
)