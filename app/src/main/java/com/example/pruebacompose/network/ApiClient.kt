package com.example.pruebacompose.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // No hay que usar 127.0.0.1 ya que está dirección hace referencia al propio emulador
    // en lugar del anfitrión. Para hacerlo correctamente se hace de la siguiente manera:
    private const val BASE_URL = "http://10.0.2.2:8000/"

    // Como la api no es https hay que crear un archivo XML de configuración en re/xml/
    // Y despues, en el manifest hacer referencia a ese archivo.

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
