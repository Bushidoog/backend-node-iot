package com.example.iot_vicente.data.remote.api

import com.example.iot_vicente.data.remote.dto.LoginRequest
import com.example.iot_vicente.data.remote.dto.LoginResponse
import com.example.iot_vicente.data.remote.dto.RegisterRequest
import com.example.iot_vicente.data.remote.dto.RegisterResponse
import com.example.iot_vicente.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    // LOGIN
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    // PROFILE (validar token)
    @GET("profile")
    suspend fun profile(
        @Header("Authorization") auth: String
    ): UserDto

    // REGISTER (lo que ya tenías)
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): RegisterResponse
}
