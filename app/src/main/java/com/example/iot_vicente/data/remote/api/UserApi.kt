package com.example.iot_vicente.data.remote.api

import com.example.iot_vicente.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Body

interface UserApi {
    @GET("api/users")
    suspend fun getUsers(): Response<List<UserDto>>

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto): Response<UserDto>

    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}