package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pruebacompose.auth.SessionManager
import com.example.pruebacompose.core.navigation.BottomNavBar
import com.example.pruebacompose.core.ui.CreateFilmFab
import com.example.pruebacompose.models.User
import com.example.pruebacompose.ui.theme.PruebaComposeTheme

@Composable
fun ProfileScreen(
    navigateToToFilms: () -> Unit,
    navigateToCreateFilm: () -> Unit,
    onLogout: () -> Unit,
) {
    val user = SessionManager.currentUser ?: User(0, "Loading...", "Loading...", "")
    // Extraemos la inicial del nombre de usuario
    val userInitial = user.username.take(1).uppercase()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navigateToFilms = navigateToToFilms,
                navigateToProfile = {},
                floatingActionButton = {
                    CreateFilmFab { navigateToCreateFilm() }
                },
            )
        },
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp), // Añadir un poco de relleno
            horizontalAlignment = Alignment.CenterHorizontally, // Centrar contenido horizontalmente
            verticalArrangement = Arrangement.Center // Centrar contenido verticalmente
        ) {
            // Círculo con la inicial del nombre
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Hacer el círculo
                    .background(MaterialTheme.colorScheme.primary) // Color de fondo del círculo
            ) {
                Text(
                    text = userInitial,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.align(Alignment.Center) // Centrar la inicial dentro del círculo
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaciado entre el círculo y el texto

            // Nombre completo
            Text(
                text = user.username,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Correo electrónico
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp)) // Espaciado entre el correo y el botón de cierre de sesión

            // Botón para cerrar sesión
            Button(onClick = onLogout) {
                Text(text = "Cerrar sesión")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    PruebaComposeTheme {
        ProfileScreen({}, {}) { }
    }
}