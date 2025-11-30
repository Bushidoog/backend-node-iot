package com.example.iot_vicente.data.remote.api

import com.example.iot_vicente.data.remote.dto.SensorDto
import retrofit2.Response
import retrofit2.http.GET

interface IotApi {
    // Ajusta esta ruta según tu backend. 
    // Si tu backend no tiene esta ruta real, el ViewModel manejará el error 
    // o puedes simular datos si prefieres no depender del backend aún.
    @GET("iot/sensors") 
    suspend fun getSensorData(): Response<SensorDto>
}