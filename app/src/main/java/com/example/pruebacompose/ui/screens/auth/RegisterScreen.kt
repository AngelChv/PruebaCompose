package com.example.pruebacompose.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pruebacompose.data.remote.ApiClient
import com.example.pruebacompose.data.repository.AuthRepository
import com.example.pruebacompose.data.service.AuthService
import com.example.pruebacompose.ui.components.EmailField
import com.example.pruebacompose.ui.components.EnableButton
import com.example.pruebacompose.ui.components.HeaderImage
import com.example.pruebacompose.ui.components.PasswordField
import com.example.pruebacompose.ui.components.UsernameField
import com.example.pruebacompose.ui.theme.PruebaComposeTheme
import com.example.pruebacompose.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    registerVM: RegisterViewModel,
    navigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro",
                        Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
            )
        }
    ) { paddingValues: PaddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Register(
                modifier = Modifier.align(Alignment.Center),
                registerVM,
                navigateToLogin,
                onRegisterSuccess,
            )
        }
    }
}

@Composable
fun Register(
    modifier: Modifier,
    registerVM: RegisterViewModel,
    navigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
) {
    val context = LocalContext.current
    val isRegistered by registerVM.isRegistered.collectAsState()
    val isLoading by registerVM.isLoading.collectAsState()
    val errorMessage by registerVM.errorMessage.collectAsState()
    val username by registerVM.username.collectAsState()
    val usernameErrorMessage by registerVM.usernameErrorMessage.collectAsState()
    val email by registerVM.email.collectAsState()
    val password by registerVM.password.collectAsState()
    val validate by registerVM.validate.collectAsState()
    val isHidingPassword by registerVM.isHidingPassword.collectAsState()

    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            Toast.makeText(context, "Cuenta creada", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
        }
    }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            HeaderImage()
        }

        errorMessage?.let {
            item {
                Text(it, color = Color.Red)
            }
        }

        item {
            UsernameField(username) { registerVM.onUserNameChange(it) }
            usernameErrorMessage?.let { Text(it, color = Color.Red) }
        }

        item {
            EmailField(email) { registerVM.onEmailChange(it) }
        }

        item {
            PasswordField(
                password,
                isHidingPassword,
                { registerVM.toggleHidePassword() }) { registerVM.onPasswordChange(it) }
        }

        item {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                EnableButton("Registrarse", validate) { registerVM.register(username, email, password) }
                TextButton(navigateToLogin) { Text("¿Ya tienes una cuenta? Iniciar Sesión") }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    val viewModel =
        RegisterViewModel(AuthRepository(ApiClient.retrofit.create(AuthService::class.java)))
    PruebaComposeTheme {
        RegisterScreen(
            registerVM = viewModel,
        )
    }
}
