package com.example.pruebacompose.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.auth0.android.jwt.JWT
import com.example.pruebacompose.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object TokenManager {
    private val TOKEN_KEY = stringPreferencesKey("jwt_token")

    fun decodeJWT(token: String): User? {
        return try {
            val jwt = JWT(token)
            val id = jwt.getClaim("id").asInt() // Nombre del claim en el JWT
            val name = jwt.getClaim("username").asString()
            val email = jwt.getClaim("email").asString()

            if (id != null && name != null && email != null) {
                User(id, name, email, token)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(context: Context): String? {
        return context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }.first()
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
}