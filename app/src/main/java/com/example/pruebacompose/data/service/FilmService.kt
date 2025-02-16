package com.example.pruebacompose.data.service

import com.example.pruebacompose.domain.model.Film
import com.example.pruebacompose.domain.usecase.FilmCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Servicio para la gestión de películas, definiendo las llamadas a la API relacionadas con la manipulación de películas.
 */
interface FilmService {
    /**
     * Obtiene la lista de todas las películas.
     *
     * @return Lista de objetos [Film] disponibles en la base de datos.
     */
    @GET("films/")
    suspend fun getFilms(): List<Film>

    /**
     * Crea una nueva película en la base de datos.
     *
     * @param film Objeto que contiene la información de la película a crear.
     * @return [Response] que contiene el ID de la película creada si la operación es exitosa.
     */
    @Headers("Content-type: application/json")
    @POST("films/create")
    suspend fun createFilm(@Body film: FilmCreate): Response<Int>

    /**
     * Actualiza la información de una película existente.
     *
     * @param film Objeto que contiene la información actualizada de la película.
     * @return [Response] que indica si la operación fue exitosa con un valor booleano.
     */
    @Headers("Content-type: application/json")
    @POST("films/update")
    suspend fun updateFilm(@Body film: Film): Response<Boolean>

    /**
     * Elimina una película de la base de datos.
     *
     * @param filmId ID de la película a eliminar.
     * @return [Response] que indica si la operación fue exitosa con un valor booleano.
     */
    @DELETE("films/{film_id}")
    suspend fun deleteFilm(@Path("film_id") filmId: Int): Response<Boolean>
}

