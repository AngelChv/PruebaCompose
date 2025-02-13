package com.example.pruebacompose.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.FilmCreate
import com.example.pruebacompose.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilmViewModel(private val repository: FilmRepository) : ViewModel() {
    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films

    private val _currentFilm = MutableStateFlow<Film?>(null)
    // No se puede hacer con un getter porque devuelvo otro tipo.
    val currentFilm: StateFlow<Film?> = _currentFilm

    fun setCurrentFilm(newFilm: Film) {
        _currentFilm.value = newFilm.copy()
    }

    fun setFilms(films: List<Film>) {
        _films.value = films
    }

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

    fun updateFilm(film: Film, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.updateFilm(film)
            result.onSuccess {
                onSuccess()
                loadFilms()
                _currentFilm.value = film.copy()
            }.onFailure { error ->
                onError(error.message ?: "Error desconocido")
            }
        }
    }

    fun deleteFilm(filmId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.deleteFilm(filmId)
            result.onSuccess {
                onSuccess()
                // Se puede hacer de otra manera más eficiente.
                loadFilms()
            }.onFailure { error ->
                onError(error.message ?: "Error desconocido")
            }
        }
    }
}
