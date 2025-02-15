package com.example.pruebacompose.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebacompose.auth.SessionManager
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
import com.example.pruebacompose.ui.screens.ProfileScreen
import com.example.pruebacompose.viewmodel.FilmViewModel
import com.example.pruebacompose.viewmodel.LoginViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    val bottomNavBar = @Composable { BottomNavBar(navController) }

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
    NavHost(navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(loginViewModel) {
                navController.navigate(Films) { popUpTo(Login) { inclusive = true } }
            }
        }

        composable<Films> {
            FilmListScreen(
                viewModel = filmViewModel,
                navigateToCreateFilm = { navController.navigate(CreateFilmForm) },
                bottomNavBar = bottomNavBar,
            ) { film: Film ->
                filmViewModel.setCurrentFilm(film)
                navController.navigate(FilmDetail)
            }
        }

        composable<FilmDetail> {
            FilmDetailScreen(viewModel = filmViewModel,
                navigateBack = { navController.popBackStack() },
                navigateToEditFilm = {
                    filmViewModel.setCurrentFilm(it)
                    navController.navigate(EditFilmForm)
                }
            )
        }

        composable<EditFilmForm> {
            FilmFormScreen(viewModel = filmViewModel) { navController.popBackStack() }
        }

        composable<CreateFilmForm> {
            filmViewModel.setCurrentFilm(null)
            FilmFormScreen(viewModel = filmViewModel) { navController.popBackStack() }
        }

        composable<Profile> {
            ProfileScreen(
                bottomNavBar = bottomNavBar,
            ) {
                SessionManager.currentUser = null
                navController.navigate(Login) {
                    popUpTo<Login> {
                        inclusive = true
                    }
                }
            }
        }
    }
}