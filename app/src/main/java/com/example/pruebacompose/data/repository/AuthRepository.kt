package com.example.pruebacompose.data.repository

import android.util.Log
import com.example.pruebacompose.domain.model.User
import com.example.pruebacompose.domain.usecase.UserLogin
import com.example.pruebacompose.domain.usecase.UserRegister
import com.example.pruebacompose.data.service.AuthService

/**
 * Repositorio que maneja la autenticación, como el login y register.
 *
 * @property service Servicio de autenticación utilizado para realizar las peticiones.
 */
class AuthRepository(private val service: AuthService) {
    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param credentials Credenciales del usuario para el inicio de sesión.
     * @return [Result] con el usuario autenticado si la operación es exitosa, o un error en caso contrario.
     */
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

    /**
     * Registra un nuevo usuario con la información proporcionada.
     *
     * @param userRegister Datos del usuario para el registro.
     * @return [Result] con el usuario registrado si la operación es exitosa, o un error en caso contrario.
     */
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

    /**
     * Verifica si un nombre de usuario ya existe en la base de datos.
     *
     * @param username Nombre de usuario a comprobar.
     * @return [Result] con `true` si el usuario existe, `false` si no existe, o un error en caso contrario.
     */
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