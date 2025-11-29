package com.example.iot_vicente.data.remote.dto

data class ForgotPasswordRequest(
    val email: String
)

data class RecoverResponse(
    val success: Boolean,
    val message: String
)

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val newPass: String
)

data class ResetPasswordResponse(
    val success: Boolean,
    val message: String
)