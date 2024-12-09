package com.example.wapp.data

data class Reviews(
    val idReview: String? = null,
    val markerId: String,
    val userId: String,
    val rating: Number,
    val review: String,
    val createdAt: String? = null
)