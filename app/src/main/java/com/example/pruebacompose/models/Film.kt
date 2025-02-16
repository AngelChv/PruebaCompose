package com.example.pruebacompose.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
    fun defaultPoster(isDarkTheme: Boolean, backgroundColor: Color, textColor: Color): String {
        val backgroundColorHex = Integer.toHexString(backgroundColor.toArgb()).removeRange(0,2)
        val textColorHex = Integer.toHexString(textColor.toArgb()).removeRange(0,2)
        return if (isDarkTheme) "https://placehold.co/400x600/$backgroundColorHex/$textColorHex/jpg?text=$title"
        else "https://placehold.co/400x600/$backgroundColorHex/$textColorHex/jpg?text=$title"
    }

    companion object {
        fun example(): Film {
            return Film(
                id = 0,
                title = "Título",
                director = "Director",
                year = 1999,
                duration = 130,
                description = "Descripción",
                posterPath = "https://placehold.co/1080x1920/EEE/31343C"
            )
        }

        fun listExample(size: Int): List<Film> {
            return List(size) { example() }
        }
    }
}