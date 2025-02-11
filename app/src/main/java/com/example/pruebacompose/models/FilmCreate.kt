package com.example.pruebacompose.models

import com.google.gson.annotations.SerializedName

// No utilizo @Serializable, porque no necesito pasar el objeto entre pantallas, para eso uso
// la otra clase Film, en este caso uso gson para pasar los datos a la api con el formato
// adecuado. Para ello modifico los nombres que son distintos en los esquemas de la api
// con @SerializedName().
data class FilmCreate(
    val title: String,
    val director: String,
    val year: Int,
    val duration: Int,
    val description: String,
    // Si el esquema de la api tiene un nombre distino es preciso indicarlo.
    @SerializedName("poster_path")
    val posterPath: String?,
)