package com.example.pruebacompose.repository

import android.util.Log
import com.example.pruebacompose.models.User
import com.example.pruebacompose.models.UserLogin
import com.example.pruebacompose.service.AuthService

class AuthRepository(private val service: AuthService) {
    suspend fun login(credentials: UserLogin): Result<User?> {
        return try {
            val response = service.login(credentials)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Log.e("AuthRepository.login()", "${response.errorBody()?.string()}")
                Result.failure(
                    Exception(
                        "Error al hacer login: ${response.errorBody()?.string()}"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("AuthRepository.login()", "Excepción al hacer login", e)
            Result.failure(e)
        }
    }
}