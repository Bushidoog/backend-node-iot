package com.example.iot_vicente.data.repository

import com.example.iot_vicente.data.remote.api.ApiClient
import com.example.iot_vicente.data.remote.dto.ForgotPasswordRequest
import com.example.iot_vicente.data.remote.dto.LoginRequest
import com.example.iot_vicente.data.remote.dto.RegisterRequest
import com.example.iot_vicente.data.remote.dto.ResetPasswordRequest

class AuthRepository {

    private val api = ApiClient.authApi

    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun register(name: String, surname: String, email: String, password: String) =
        api.register(RegisterRequest(name, surname, email, password))

    suspend fun forgotPassword(email: String) =
        api.forgotPassword(ForgotPasswordRequest(email))

    suspend fun resetPassword(email: String, code: String, newPass: String) =
        api.resetPassword(ResetPasswordRequest(email, code, newPass))
}