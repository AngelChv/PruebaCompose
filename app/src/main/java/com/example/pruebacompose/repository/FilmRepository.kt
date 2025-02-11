package com.example.pruebacompose.repository

import android.util.Log
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.FilmCreate
import com.example.pruebacompose.network.ApiService

class FilmRepository(private val apiService: ApiService) {
    suspend fun getFilms(): List<Film> {
        return apiService.getFilms()
    }

    suspend fun createFilm(film: FilmCreate): Result<Int> {
        return try {
            val response = apiService.createFilm(film)
            if (response.isSuccessful) {
                Result.success(response.body() ?: -1)
            } else {
                Log.e("FilmRepository.createFilm()", "${response.errorBody()?.string()}")
                Result.failure(Exception("Error al crear película: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
