package com.example.iot_vicente.data.repository

import com.example.iot_vicente.data.remote.api.AuthApi
import com.example.iot_vicente.data.remote.dto.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val api: AuthApi) {

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
}