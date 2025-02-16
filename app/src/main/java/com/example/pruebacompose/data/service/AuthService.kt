package com.example.pruebacompose.data.service

import com.example.pruebacompose.domain.model.User
import com.example.pruebacompose.domain.usecase.UserLogin
import com.example.pruebacompose.domain.usecase.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Servicio de autenticación que define las llamadas a la API relacionadas con la autenticación y
 * gestión de usuarios.
 *
 * Es utilizada por retrofit para generar las peticiones en función de la definición realizada.
 */
interface AuthService {
    /**
     * Realiza la autenticación de un usuario con sus credenciales.
     *
     * @param credentials Objeto que contiene el nombre de usuario y la contraseña.
     * @return [Response] que contiene los datos del usuario autenticado si la operación es exitosa.
     */
    @POST("/users/login")
    suspend fun login(@Body credentials: UserLogin): Response<User>

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRegister Objeto con la información necesaria para el registro del usuario.
     * @return [Response] que contiene los datos del usuario registrado si la operación es exitosa.
     */
    @POST("/users/register")
    suspend fun register(@Body userRegister: UserRegister): Response<User>

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario a buscar.
     * @return [Response] que contiene los datos del usuario si existe, o null si no se encuentra.
     */
    @GET("/users/{username}")
    suspend fun findByUsername(@Path("username") username: String): Response<User?>
}