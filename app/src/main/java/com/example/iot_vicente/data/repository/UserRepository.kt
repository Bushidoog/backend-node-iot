package com.example.iot_vicente.data.repository

import com.example.iot_vicente.data.remote.api.ApiClient
import com.example.iot_vicente.data.remote.dto.UserDto

class UserRepository {
    private val api = ApiClient.userApi

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: UserDto): Result<UserDto> {
        return try {
            val response = api.updateUser(user.id, user)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: Int): Result<Unit> {
        return try {
            val response = api.deleteUser(userId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar usuario: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}