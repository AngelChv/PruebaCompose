package com.example.pruebacompose.ui.screens.auth

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
import com.example.pruebacompose.ui.components.EnableButton
import com.example.pruebacompose.ui.components.HeaderImage
import com.example.pruebacompose.ui.components.PasswordField
import com.example.pruebacompose.ui.components.UsernameField
import com.example.pruebacompose.ui.theme.PruebaComposeTheme
import com.example.pruebacompose.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginVM: LoginViewModel,
    navigateToRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Inicio de Sesión",
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
            Login(
                modifier = Modifier
                    .align(Alignment.Center),
                loginVM,
                navigateToRegister,
                onLoginSuccess,
            )
        }
    }
}

@Composable
fun Login(
    modifier: Modifier,
    loginVM: LoginViewModel,
    navigateToRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
) {
    val context = LocalContext.current
    val isLoggedIn by loginVM.isLoggedIn.collectAsState()
    val isLoading by loginVM.isLoading.collectAsState()
    val errorMessage by loginVM.errorMessage.collectAsState()
    val username by loginVM.username.collectAsState()
    val password by loginVM.password.collectAsState()
    val validate by loginVM.validate.collectAsState()
    val isHidingPassword by loginVM.isHidingPassword.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onLoginSuccess()
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
            UsernameField(username) { loginVM.onUserNameChange(it) }
        }
        item {
            PasswordField(
                password,
                isHidingPassword,
                { loginVM.toggleHidePassword() }) { loginVM.onPasswordChange(it) }
        }
        item {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                EnableButton(validate) { loginVM.login(context, username, password) }
                TextButton(navigateToRegister) { Text("¿No tienes cuenta? Registrarse") }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    val viewModel =
        LoginViewModel(AuthRepository(ApiClient.retrofit.create(AuthService::class.java)))
    PruebaComposeTheme {
        LoginScreen(
            viewModel,
        )
    }
}