package com.example.kashifapp.auth.domain.model

data class User(
    val id: String,
    val email: String,
    val displayName: String?,
    val photoUrl: String?
)
