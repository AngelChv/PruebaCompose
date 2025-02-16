package com.example.pruebacompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pruebacompose.models.UserRegister
import com.example.pruebacompose.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _userNameErrorMessage = MutableStateFlow<String?>(null)
    val usernameErrorMessage: StateFlow<String?> = _userNameErrorMessage

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _validate = MutableStateFlow(false)
    val validate: StateFlow<Boolean> = _validate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    private val _isHidingPassword = MutableStateFlow(true)
    val isHidingPassword: StateFlow<Boolean> = _isHidingPassword

    fun onUserNameChange(username: String) {
        _username.value = username
        validate()
    }

    fun onEmailChange(email: String) {
        _email.value = email
        validate()
    }

    fun onPasswordChange(password: String) {
        _password.value = password
        validate()
    }

    fun toggleHidePassword() {
        _isHidingPassword.value = !_isHidingPassword.value
    }

    fun register(username: String, email: String, password: String) {
        if (_validate.value) {
            viewModelScope.launch {
                _isLoading.value = true
                _errorMessage.value = null
                val result = authRepository.register(UserRegister(username, email, password))
                result.onSuccess {
                    _isRegistered.value = true
                }.onFailure {
                    _errorMessage.value = it.message ?: "Error desconocido"
                }
                _isLoading.value = false
            }
        } else {
            _errorMessage.value = "El formulario no es correcto"
        }
    }

    private fun validate() {
        viewModelScope.launch {
            _validate.value = isValidUserName(_username.value) &&
                    isValidEmail(_email.value) &&
                    isValidPassword(_password.value)
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 6
    }

    private suspend fun isValidUserName(username: String): Boolean {
        return username.isNotBlank() && !existUsername(username)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private suspend fun existUsername(username: String): Boolean {
        val result = authRepository.existUsername(username)
        if (result.isSuccess) {
            val exists = result.getOrNull() ?: false
            if (exists) {
                _userNameErrorMessage.value = "El nombre de usuario ya existe"
            } else {
                _userNameErrorMessage.value = null
            }
            return exists
        } else {
            _userNameErrorMessage.value = null
            return false
        }
    }
}

class RegisterViewModelFactory(
    private val authRepository: AuthRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
