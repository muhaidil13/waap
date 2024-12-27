package com.example.wapp.data


data class User(
    val id: String,
    val name:  String,
    val email:  String,
    val phone: String,
    val role: String = "user",
    val createdAt: String,
    val lastLogin: String,
    val updatedAt: String,
)