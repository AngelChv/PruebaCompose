package com.example.pruebacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.network.ApiClient
import com.example.pruebacompose.repository.AuthRepository
import com.example.pruebacompose.repository.FilmRepository
import com.example.pruebacompose.service.AuthService
import com.example.pruebacompose.service.FilmService
import com.example.pruebacompose.ui.screens.FilmDetailScreen
import com.example.pruebacompose.ui.screens.FilmFormScreen
import com.example.pruebacompose.ui.screens.FilmListScreen
import com.example.pruebacompose.ui.screens.LoginScreen
import com.example.pruebacompose.viewmodel.FilmViewModel
import com.example.pruebacompose.viewmodel.LoginViewModel
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainNavHost()
        }
    }
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    // Crear servicios:
    val authService = ApiClient.retrofit.create(AuthService::class.java)
    val filmService = ApiClient.retrofit.create(FilmService::class.java)

    // Crear repositorios:
    val authRepository = AuthRepository(authService)
    val filmRepository = FilmRepository(filmService)

    // Crear View Models:
    // Evitar que haya varias instancias del viewModel pasando la misma manualmente a las pantallas.
    val loginViewModel = LoginViewModel(authRepository)
    val filmViewModel = FilmViewModel(filmRepository)

    // Crear anfitrión de navegación:
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(loginViewModel) {
                navController.navigate("films") { popUpTo("login") { inclusive = true } }
            }
        }

        composable("films") {
            // Antes lo hacia de esta manera, ahora no paso el viewModel a FilmListScreen
            // Si no que utiliza el valor por defecto de viewModel()
            // Crear instancia de ApiService a través de retrofit.
            //val apiService = ApiClient.retrofit.create(ApiService::class.java)

            // Crear el repositorio pasando la implementación de ApiService
            //val repository = FilmRepository(apiService)

            // Crear el ViewModel pasando el repositorio
            // Estoy haciendolo de manera manual, como mejora sería usar un Factory o un Framework
            // de inyección.
            //val viewModel = FilmViewModel(repository)

            FilmListScreen(navController, viewModel = filmViewModel) { film: Film ->
                filmViewModel.setCurrentFilm(film)
                navController.navigate("filmDetail")
            }
        }

        composable(
            "filmDetail",
        ) {
            FilmDetailScreen(navController, viewModel = filmViewModel)
        }

        composable("editFilmForm/{filmJson}") { backStackEntry: NavBackStackEntry ->
            val filmJson = backStackEntry.arguments?.getString("filmJson")
            // Si el json no es nulo se transforma a Film.
            val film = filmJson?.let { Json.decodeFromString<Film>(it) }
            FilmFormScreen(navController = navController, film = film, viewModel = filmViewModel)
        }

        composable("createFilmForm") {
            FilmFormScreen(navController = navController, viewModel = filmViewModel)
        }
    }
}

@Preview
@Composable
fun PreviewApp() {
    MainNavHost()
}