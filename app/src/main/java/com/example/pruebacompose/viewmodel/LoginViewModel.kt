package com.example.pruebacompose.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebacompose.auth.SessionManager
import com.example.pruebacompose.models.UserLogin
import com.example.pruebacompose.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _validate = MutableStateFlow(false)
    val validate: StateFlow<Boolean> = _validate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun onUserNameChange(username: String) {
        _username.value = username
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        validate()
    }

    private fun validate() {
        _validate.value = isValidPassword(_password.value)
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 6
    }


    // todo: refactorizar
    fun login(context: Context, username: String, password: String) {
        if (_validate.value) {
            viewModelScope.launch {
                _isLoading.value = true
                _errorMessage.value = null
                val result = authRepository.login(UserLogin(username, password))
                result.onSuccess { user ->
                    if (user != null) {
                        SessionManager.login(context, user)
                        _isLoggedIn.value = true
                    } else {
                        _errorMessage.value = "Usuario o contraseña incorrectos"
                    }
                }.onFailure {
                    _errorMessage.value = it.message ?: "Error desconocido"
                }
                _isLoading.value = false
            }
        } else {
            _errorMessage.value = "El formulario no es correcto"
        }
    }
}