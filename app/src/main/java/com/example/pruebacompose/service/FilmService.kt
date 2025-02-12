package com.example.pruebacompose.service

import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.FilmCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface FilmService {
    @GET("films/") // Endpoint de la API
    suspend fun getFilms(): List<Film>

    @Headers("Content-type: application/json")
    @POST("films/create")
    suspend fun createFilm(@Body film: FilmCreate): Response<Int>

    @Headers("Content-type: application/json")
    @POST("films/update")
    suspend fun updateFilm(@Body film: Film): Response<Boolean>

    @DELETE("films/{film_id}")
    suspend fun deleteFilm(@Path("film_id") filmId: Int): Response<Boolean>
}
