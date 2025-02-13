package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pruebacompose.R
import com.example.pruebacompose.network.ApiClient
import com.example.pruebacompose.repository.AuthRepository
import com.example.pruebacompose.service.AuthService
import com.example.pruebacompose.ui.theme.PruebaComposeTheme
import com.example.pruebacompose.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    loginVM: LoginViewModel,
    onLoginSuccess: () -> Unit,
) {
    Scaffold { paddingValues: PaddingValues ->
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
                onLoginSuccess,
            )
        }
    }
}

@Composable
fun Login(
    modifier: Modifier,
    loginVM: LoginViewModel,
    onLoginSuccess: () -> Unit,
) {
    val isLoggedIn by loginVM.isLoggedIn.collectAsState()
    val isLoading by loginVM.isLoading.collectAsState()
    val errorMessage by loginVM.errorMessage.collectAsState()
    val username by loginVM.username.collectAsState()
    val password by loginVM.password.collectAsState()
    val validate by loginVM.validate.collectAsState()

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
            PasswordField(password) { loginVM.onPasswordChange(it) }
        }
        item {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LoginButton(validate) { loginVM.login(username, password) }
            }
        }
    }
}

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(R.drawable.chv), contentDescription = "Logo",
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun UsernameField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        label = { Text("Nombre de usuario") }
    )
}

@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        maxLines = 1,
        label = { Text("Contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun LoginButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text("Iniciar sesión")
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
            onLoginSuccess = {}
        )
    }
}