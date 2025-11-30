package com.example.LC_App.data.remote.api

import com.example.LC_App.data.remote.dto.ForgotPasswordRequest
import com.example.LC_App.data.remote.dto.LoginRequest
import com.example.LC_App.data.remote.dto.LoginResponse
import com.example.LC_App.data.remote.dto.RecoverResponse
import com.example.LC_App.data.remote.dto.RegisterRequest
import com.example.LC_App.data.remote.dto.RegisterResponse
import com.example.LC_App.data.remote.dto.ResetPasswordRequest
import com.example.LC_App.data.remote.dto.ResetPasswordResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): RecoverResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse
}