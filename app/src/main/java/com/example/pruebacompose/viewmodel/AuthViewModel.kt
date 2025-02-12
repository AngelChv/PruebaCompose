package com.example.pruebacompose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pruebacompose.models.User
import com.example.pruebacompose.models.UserLogin
import com.example.pruebacompose.network.ApiClient
import com.example.pruebacompose.repository.AuthRepository
import com.example.pruebacompose.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val authService = ApiClient.retrofit.create(AuthService::class.java)
    private val repository = AuthRepository(authService)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    suspend fun login(
        credentials: UserLogin,
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit,
    ) {
        val result = repository.login(credentials)
        result.onSuccess {
            _user.value = result.getOrNull()
            onSuccess(_user.value != null)
        }.onFailure {
            onError(it.message ?: "Error desconocido")
        }
    }
}