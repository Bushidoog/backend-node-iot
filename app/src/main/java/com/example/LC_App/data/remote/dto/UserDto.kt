package com.example.LC_App.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,

    // Esta es la clave para ver los apellidos:
    // Le decimos a Gson: "Busca el campo 'last_name' en el JSON que manda Node.js"
    @SerializedName("last_name")
    val last_name: String? // Puede ser null si el usuario es viejo
)