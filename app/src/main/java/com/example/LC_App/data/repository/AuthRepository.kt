package com.example.LC_App.data.repository


import android.content.Context
import com.example.LC_App.data.local.TokenStorage // Tu objeto DataStore
import com.example.LC_App.data.remote.api.ApiClient
import com.example.LC_App.data.remote.dto.*
import retrofit2.Response

class AuthRepository(private val context: Context) {

    private val api = ApiClient.authApi

    // LOGIN: Guardamos el token si la respuesta es exitosa
    suspend fun login(email: String, pass: String): Response<LoginResponse> {
        val response = api.login(LoginRequest(email, pass))

        if (response.isSuccessful) {
            val token = response.body()?.token
            if (!token.isNullOrEmpty()) {
                // AQUÍ USAMOS TU TOKENSTORAGE
                TokenStorage.saveToken(context, token)
            }
        }
        return response
    }

    // REGISTRO
    suspend fun register(name: String, surname: String, email: String, pass: String) =
        api.register(RegisterRequest(name, surname, email, pass))

    // RECUPERAR CLAVE
    suspend fun forgotPassword(email: String) =
        api.forgotPassword(ForgotPasswordRequest(email))

    suspend fun resetPassword(email: String, code: String, newPass: String) =
        api.resetPassword(ResetPasswordRequest(email, code, newPass))

    // Función para cerrar sesión (Útil para un botón de Logout)
    suspend fun logout() {
        TokenStorage.clearToken(context)
    }
}