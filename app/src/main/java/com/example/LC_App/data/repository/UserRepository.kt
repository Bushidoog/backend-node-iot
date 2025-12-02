package com.example.LC_App.data.repository

import android.content.Context
import com.example.LC_App.data.remote.api.ApiClient
import com.example.LC_App.data.remote.dto.UserDto

class UserRepository(private val context: Context)  {
    private val api = ApiClient.getAuthenticatedUserApi(context)

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
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

    // 1. Obtener usuario por ID
    suspend fun getUserById(id: Int): Result<UserDto> {
        return try {
            val response = api.getUserById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error 404: Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 2. Actualizar usuario
    suspend fun updateUser(id: Int, user: UserDto): Result<Unit> {
        return try {
            val response = api.updateUser(id, user)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al actualizar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
