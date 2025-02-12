package com.example.pruebacompose.network

import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.FilmCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @GET("films/") // Endpoint de la API
    suspend fun getFilms(): List<Film>

    @Headers("Content-type: application/json")
    @POST("films/create")
    suspend fun createFilm(@Body film: FilmCreate): Response<Int>
}
