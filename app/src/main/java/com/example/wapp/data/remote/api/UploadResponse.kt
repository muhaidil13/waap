package com.example.wapp.data.remote.api

data class UploadResponse(
    val success: Boolean,
    val message: String,
    val fileUrl: String?
)