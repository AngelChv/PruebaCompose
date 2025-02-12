package com.example.pruebacompose.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// Utilizo @Serializable con json para pasar el objeto entre pantallas.
// No es lo mismo que utilizo con gson para pasar los datos a la api.
@Serializable
data class Film(
    val id: Int,
    val title: String,
    val director: String,
    val year: Int,
    val duration: Int,
    val description: String,
    // Si el esquema de la api tiene un nombre distino es preciso indicarlo.
    // Esta etiqueta es de gson
    @SerializedName("poster_path")
    val posterPath: String?,
) {
    companion object {
        fun example(): Film {
            return Film(
                id = 0,
                title = "Título",
                director = "Director",
                year = 1999,
                duration = 130,
                description = "Descripción",
                posterPath = "poster path"
            )
        }

        fun listExample(size: Int): List<Film> {
            return List(size) { example() }
        }
    }
}