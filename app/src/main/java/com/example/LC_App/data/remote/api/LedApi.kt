package com.example.LC_App.data.remote.api

import com.example.LC_App.data.remote.dto.LedDto
import com.example.LC_App.data.remote.dto.LedUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface LedApi {
    @GET("api/leds")
    suspend fun getLeds(): List<LedDto>

    @PUT("api/leds/{id}")
    suspend fun updateLed(
        @Path("id") id: Int,
        @Body request: LedUpdateRequest
    ): LedDto
}
