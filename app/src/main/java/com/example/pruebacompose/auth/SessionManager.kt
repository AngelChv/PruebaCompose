package com.example.pruebacompose.auth

import android.content.Context
import com.example.pruebacompose.models.User
import com.example.pruebacompose.network.TokenManager

object SessionManager {
    var currentUser: User? = null

    suspend fun loadSession(
        context: Context,
        onError: () -> Unit,
        onSuccess: () -> Unit,
    ) {
        val token = TokenManager.getToken(context)
        if (token.isNullOrEmpty()) {
            onError()
        } else {
            currentUser = TokenManager.decodeJWT(token)
            if (currentUser != null) onSuccess()
            else onError()
        }
    }

    suspend fun login(context: Context, user: User) {
        currentUser = user
        TokenManager.saveToken(context, user.token)
    }

    suspend fun logout(context: Context) {
        currentUser = null
        TokenManager.clearToken(context)
    }
}