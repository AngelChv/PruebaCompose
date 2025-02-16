package com.example.pruebacompose.data.repository

import android.util.Log
import com.example.pruebacompose.domain.model.Film
import com.example.pruebacompose.domain.usecase.FilmCreate
import com.example.pruebacompose.data.service.FilmService

/**
 * Repositorio para gestionar las películas en la aplicación.
 * Se encarga de realizar operaciones CRUD (crear, leer, actualizar y eliminar)
 * mediante el servicio de películas.
 *
 * @property filmService Servicio de películas para interactuar con la API.
 */
class FilmRepository(private val filmService: FilmService) {
    /**
     * Obtiene la lista de películas disponibles.
     *
     * @return Lista de películas obtenidas de la API.
     */
    suspend fun getFilms(): List<Film> {
        return filmService.getFilms()
    }

    /**
     * Crea una nueva película en la base de datos.
     *
     * @param film Objeto con los datos de la película a crear.
     * @return [Result] con el ID de la película creada si la operación es exitosa, o un error en caso contrario.
     */
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

    /**
    * Actualiza la información de una película existente.
    *
    * @param film Objeto con los datos actualizados de la película.
    * @return [Result] con `true` si la operación es exitosa, o un error en caso contrario.
    */
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

    /**
     * Elimina una película de la base de datos.
     *
     * @param filmId ID de la película a eliminar.
     * @return [Result] con `true` si la operación es exitosa, o un error en caso contrario.
     */
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
