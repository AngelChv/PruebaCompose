package com.example.pruebacompose.data.repository

import android.util.Log
import com.example.pruebacompose.domain.model.User
import com.example.pruebacompose.domain.usecase.UserLogin
import com.example.pruebacompose.domain.usecase.UserRegister
import com.example.pruebacompose.data.service.AuthService

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

    suspend fun register(userRegister: UserRegister): Result<User?> {
        return try {
            val response = service.register(userRegister)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Log.e("AuthRepository.register()", "${response.errorBody()?.string()}")
                Result.failure(
                    Exception(
                        "Error al registrarse: ${response.errorBody()?.string()}"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("AuthRepository.register()", "Excepción al registrar un usuario", e)
            Result.failure(e)
        }
    }

    suspend fun existUsername(username: String): Result<Boolean> {
        return try {
            val response = service.findByUsername(username)
            if (response.isSuccessful) {
                return Result.success(response.body() != null)
            } else {
                Log.e("AuthRepository.existUsername()", "${response.errorBody()?.string()}")
                Result.failure(
                    Exception(
                        "Error al comprobar el nombre de usuario: ${response.errorBody()?.string()}"
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(
                "AuthRepository.existUsername()",
                "Excepción al comprobar el nombre de usuario",
                e
            )
            Result.failure(e)
        }
    }
}