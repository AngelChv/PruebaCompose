package com.example.pruebacompose.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebacompose.data.local.SessionManager
import com.example.pruebacompose.domain.model.Film
import com.example.pruebacompose.data.remote.ApiClient
import com.example.pruebacompose.data.repository.AuthRepository
import com.example.pruebacompose.data.repository.FilmRepository
import com.example.pruebacompose.data.service.AuthService
import com.example.pruebacompose.data.service.FilmService
import com.example.pruebacompose.ui.screens.films.FilmDetailScreen
import com.example.pruebacompose.ui.screens.films.FilmFormScreen
import com.example.pruebacompose.ui.screens.films.FilmListScreen
import com.example.pruebacompose.ui.screens.LoadingScreen
import com.example.pruebacompose.ui.screens.auth.LoginScreen
import com.example.pruebacompose.ui.screens.profile.ProfileScreen
import com.example.pruebacompose.ui.screens.auth.RegisterScreen
import com.example.pruebacompose.viewmodel.FilmViewModel
import com.example.pruebacompose.viewmodel.FilmViewModelFactory
import com.example.pruebacompose.viewmodel.LoginViewModel
import com.example.pruebacompose.viewmodel.LoginViewModelFactory
import com.example.pruebacompose.viewmodel.RegisterViewModel
import com.example.pruebacompose.viewmodel.RegisterViewModelFactory
import kotlinx.coroutines.launch

/**
 * Gestiona toda la navegación de la app.
 *
 * Crea la estructura de pantallas y como se navega entre ellas.
 * Instancia los servicios, repositorios y viewmodels para unificarlos en toda la aplicación.
 */
@Composable
fun NavigationWrapper() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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
    val filmViewModel: FilmViewModel = viewModel(factory = FilmViewModelFactory(filmRepository))

    // Crear anfitrión de navegación:
    NavHost(navController, startDestination = Loading) {
        composable<Loading> {
            LoadingScreen()
            LaunchedEffect(Unit) {
                SessionManager.loadSession(
                    context = context,
                    onError = {
                        navController.navigate(Login) {
                            popUpTo<Loading> {
                                inclusive = true
                            }
                        }
                    },
                    onSuccess = {
                        navController.navigate(Films) {
                            popUpTo<Loading> {
                                inclusive = true
                            }
                        }
                    },
                )
            }
        }

        composable<Login> {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(authRepository)
            )
            LoginScreen(
                loginViewModel,
                navigateToRegister = {
                    navController.navigate(Register)
                },
            ) {
                navController.navigate(Films) { popUpTo(Login) { inclusive = true } }
            }
        }

        composable<Register> {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(authRepository)
            )
            RegisterScreen(
                registerViewModel,
                navigateToLogin = {
                    navController.navigateUp()
                }
            ) {
                navController.navigateUp()
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
                // Si usaba popBackStack y pulsaba rapido el botón de ir hacia atrás dos veces
                // la aplicación se bloqueaba.
                navigateBack = { navController.navigateUp() },
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
                navController.navigate(Login) {
                    popUpTo<Profile> {
                        inclusive = true
                    }
                }
                coroutineScope.launch {
                    SessionManager.logout(context)
                }
            }
        }
    }
}