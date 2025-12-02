package com.example.LC_App.data.remote.api

import com.example.LC_App.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    // 1. Obtener lista completa (Esta es la que te falta)
    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    // 2. Obtener un solo usuario por ID
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UserDto>

    // 3. Actualizar usuario
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto): Response<UserDto>

    // 4. Borrar usuario
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}