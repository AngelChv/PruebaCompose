package com.example.pruebacompose.domain.usecase

import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    val username: String,
    val email: String,
    val password: String
)
