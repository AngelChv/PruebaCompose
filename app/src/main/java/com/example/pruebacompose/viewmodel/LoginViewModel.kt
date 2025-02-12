package com.example.pruebacompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebacompose.models.UserLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authVM: AuthViewModel) : ViewModel() {
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

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            if (_validate.value) {
                authVM.login(
                    UserLogin(username, password),
                    onSuccess = { isLoggedIn: Boolean ->
                        if (isLoggedIn) {
                            _isLoggedIn.value = true
                        } else {
                            _errorMessage.value = "Usuario o contraseña incorrectos"
                        }
                    },
                    onError = {
                        _errorMessage.value = it
                    },
                )
                _isLoading.value = false
            } else {
                _errorMessage.value = "El formulario no es correcto"
            }
            _isLoading.value = false
        }
    }
}