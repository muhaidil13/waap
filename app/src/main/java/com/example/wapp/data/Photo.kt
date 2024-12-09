package com.example.wapp.data

data class Photo(
    val id: String? = null,
    val markerId: String,
    val uploadedBy: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
)