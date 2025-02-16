package com.example.pruebacompose.data.repository

import android.util.Log
import com.example.pruebacompose.domain.model.Film
import com.example.pruebacompose.domain.usecase.FilmCreate
import com.example.pruebacompose.data.service.FilmService

class FilmRepository(private val filmService: FilmService) {
    suspend fun getFilms(): List<Film> {
        return filmService.getFilms()
    }

    suspend fun createFilm(film: FilmCreate): Result<Int> {
        return try {
            val response = filmService.createFilm(film)
            if (response.isSuccessful) {
                Result.success(response.body() ?: -1)
            } else {
                Log.e("FilmRepository.createFilm()", "${response.errorBody()?.string()}")
                Result.failure(Exception("Error al crear la película: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("FilmRepository.createFilm()", "Excepción al crear la película", e)
            Result.failure(e)
        }
    }

    suspend fun updateFilm(film: Film): Result<Boolean> {
        return try {
            val response = filmService.updateFilm(film)
            if (response.isSuccessful) {
                Result.success(response.body() ?: false)
            } else {
                Log.e("FilmRepository.createFilm()", "${response.errorBody()?.string()}")
                Result.failure(Exception("Error al actualizar la película: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("FilmRepository.createFilm()", "Excepción al actualizar la película", e)
            Result.failure(e)
        }
    }

    suspend fun deleteFilm(filmId: Int): Result<Boolean> {
        return try {
            val response = filmService.deleteFilm(filmId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: false)
            } else {
                Log.e("FilmRepository.deleteFilm()", "${response.errorBody()?.string()}")
                Result.failure(Exception("Error al eliminar la película: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("FilmRepository.deleteFilm()", "Excepción al eliminar la película", e)
            Result.failure(e)
        }
    }
}
