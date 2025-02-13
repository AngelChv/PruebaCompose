package com.example.pruebacompose.network

import com.example.pruebacompose.auth.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Cada vez que se realiza una petición a la api esta clase la intercepta.
 *
 * Se introduce el token en la cabecera en cada petición.
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtener petición original
        val originalRequest = chain.request()

        // Obtener token
        val token = SessionManager.currentUser?.token

        // Solo se añade el token a la cabecera si el usuario y el token no son nulos.
        val newRequest = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        // Continuar con la petición.
        return chain.proceed(newRequest)
    }
}
