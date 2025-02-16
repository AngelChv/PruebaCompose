package com.example.pruebacompose.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    val username: String,
    val email: String,
    val password: String
)
