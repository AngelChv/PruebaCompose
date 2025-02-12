package com.example.pruebacompose.service

import com.example.pruebacompose.models.User
import com.example.pruebacompose.models.UserLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/users/login")
    suspend fun login(@Body credentials: UserLogin): Response<User>
}