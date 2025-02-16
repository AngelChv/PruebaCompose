package com.example.pruebacompose.service

import com.example.pruebacompose.models.User
import com.example.pruebacompose.models.UserLogin
import com.example.pruebacompose.models.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("/users/login")
    suspend fun login(@Body credentials: UserLogin): Response<User>

    @POST("/users/register")
    suspend fun register(@Body userRegister: UserRegister): Response<User>

    @GET("/users/{username}")
    suspend fun findByUsername(@Path("username") username: String): Response<User?>
}