package com.example.iot_vicente.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val pass: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?
)