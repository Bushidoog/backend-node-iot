package com.example.LC_App.data.repository

import com.example.LC_App.data.remote.api.ApiClient
import com.example.LC_App.data.remote.dto.ForgotPasswordRequest
import com.example.LC_App.data.remote.dto.LoginRequest
import com.example.LC_App.data.remote.dto.RegisterRequest
import com.example.LC_App.data.remote.dto.ResetPasswordRequest

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