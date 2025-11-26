package com.example.iot_vicente.data.repository

import android.content.Context
import com.example.iot_vicente.data.local.TokenStorage
import com.example.iot_vicente.data.remote.HttpClient
import com.example.iot_vicente.data.remote.api.AuthApi
import com.example.iot_vicente.data.remote.dto.LoginRequest
import com.example.iot_vicente.data.remote.dto.LoginResponse
import com.example.iot_vicente.data.remote.dto.RegisterRequest
import com.example.iot_vicente.data.remote.dto.UserDto

class AuthRepository(
    private val api: AuthApi = HttpClient.authApi   // usa el HttpClient
) {
    // ---------- LOGIN ----------
    suspend fun login(ctx: Context, email: String, password: String): Result<LoginResponse> {
        return try {
            val body = LoginRequest(email = email, password = password)
            val response = api.login(body)

            if (!response.success) {
                return Result.failure(Exception("Credenciales inválidas"))
            }

            // guarda token
            TokenStorage.saveToken(ctx, response.token)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- REGISTER (tu guía anterior) ----------
    suspend fun register(name: String, email: String, password: String): Result<String> {
        return try {
            val body = RegisterRequest(name, email, password)
            val response = api.register(body)
            if (!response.success) {
                return Result.failure(Exception(response.message))
            }
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- TOKEN ----------
    suspend fun getStoredToken(ctx: Context): String? = TokenStorage.getToken(ctx)

    suspend fun clearToken(ctx: Context) {
        TokenStorage.clearToken(ctx)
    }

    // ---------- VALIDAR TOKEN (GET /profile) ----------
    suspend fun validateToken(ctx: Context): Result<UserDto> {
        return try {
            val token = getStoredToken(ctx)
                ?: return Result.failure(Exception("Sin token guardado"))

            val user = api.profile("Bearer $token")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
