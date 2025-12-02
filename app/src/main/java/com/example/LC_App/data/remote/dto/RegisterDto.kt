package com.example.LC_App.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String, // Changed from 'surname' to 'last_name'
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
    // 'phone' was removed as it is not expected by the backend
)

data class RegisterResponse(
    // El backend devuelve "message" o "error". Mapeamos "error" a "message" para consistencia o usamos un DTO flexible.
    // Pero para RegisterResponse normal (éxito): { message: "...", user: {...} }
    // Para error 400/409: { error: "..." }
    val message: String?,
    val error: String?, // Agregado para capturar mensajes de error del backend
    val user: UserDto?
) {
    // Propiedad helper para obtener el mensaje a mostrar, sea éxito o error
    val displayMessage: String
        get() = error ?: message ?: "Respuesta desconocida"
        
    val success: Boolean
        get() = error == null
}

