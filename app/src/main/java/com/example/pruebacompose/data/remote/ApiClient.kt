package com.example.pruebacompose.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Centraliza la configuración de retrofit para contectarse a la api
 */
object ApiClient {
    // No hay que usar 127.0.0.1 ya que está dirección hace referencia al propio emulador
    // en lugar del anfitrión. Para hacerlo correctamente se hace de la siguiente manera:
    private const val BASE_URL = "http://10.0.2.2:8000/"

    // Como la api no es https hay que crear un archivo XML de configuración en re/xml/
    // Y despues, en el manifest hacer referencia a ese archivo.

    // Cliente http que utiliza retrofit
    // Al que le añado el interceptor (para incluir el token en la cabecera).
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    // Crear instancia de retrofit con la configuración indicada.
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // Uso el OkHttpClient con el interceptor
        // Permite convertir de objetos a json y viceversa en las peticiones y respuestas.
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
