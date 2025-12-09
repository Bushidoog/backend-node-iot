package com.example.LC_App.data.remote.dto

data class LedDto(
    val id: Int,
    val name: String,
    val state: Boolean
)

data class LedUpdateRequest(
    val state: Boolean
)
