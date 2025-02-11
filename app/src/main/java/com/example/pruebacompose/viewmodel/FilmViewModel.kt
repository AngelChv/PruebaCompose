package com.example.pruebacompose.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.FilmCreate
import com.example.pruebacompose.network.ApiClient
import com.example.pruebacompose.network.ApiService
import com.example.pruebacompose.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilmViewModel : ViewModel() {
    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films
    // Crear instancia de ApiService a través de retrofit.
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    // Crear el repositorio pasando la implementación de ApiService
    private val repository = FilmRepository(apiService)

    fun loadFilms() {
        viewModelScope.launch {
            try {
                _films.value = repository.getFilms()
            } catch (e: Exception) {
                Log.e("FilmViewModel", "Error al cargar películas", e)
            }
        }
    }

    fun createFilm(film: FilmCreate, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.createFilm(film)
            result.onSuccess {
                onSuccess()
                loadFilms() // Recarga la lista para mostrar la nueva película
            }.onFailure { error ->
                onError(error.message ?: "Error desconocido")
            }
        }
    }
}
