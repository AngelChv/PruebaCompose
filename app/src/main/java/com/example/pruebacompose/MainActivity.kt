package com.example.pruebacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.jsonToFilm
import com.example.pruebacompose.ui.screens.AddFilmScreen
import com.example.pruebacompose.ui.screens.FilmDetailScreen
import com.example.pruebacompose.ui.screens.FilmListScreen
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmManagerApp()
        }
    }
}

@Composable
fun FilmManagerApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "films") {
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

            FilmListScreen(navController) { film: Film ->
                // Para pasar el objeto a la pantalla detalles existen varias formas:
                // 1. Utilizar SharedViewModel.
                // 2. Pasarlo como JSON.
                // Por simplicidad voy a usar la última
                // Importante, importar las dependencias:
                // He buscado en kotlin ibraries: serialization
                val filmJson = Json.encodeToString(film)
                navController.navigate("filmDetail/$filmJson")
            }
        }
        composable(
            "filmDetail/{filmJson}",
            arguments = listOf(navArgument("filmJson") { type = NavType.StringType })
        ) { backStackEntry: NavBackStackEntry ->
            // Obtener json de los argumentos de la ruta
            val filmJson = backStackEntry.arguments?.getString("filmJson")

            // Convertir string JSON a Film (requiere kotlin.serialization)
            filmJson?.let {
                //val film = Json.decodeFromString<Film>(it)
                val film = jsonToFilm(it)
                FilmDetailScreen(film = film)
            }
        }

        composable("addFilm") {
            AddFilmScreen(navController = navController)
        }

    }



}

@Preview
@Composable
fun PreviewApp() {
    FilmManagerApp()
}