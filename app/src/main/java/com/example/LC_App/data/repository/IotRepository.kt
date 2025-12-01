package com.example.LC_App.data.repository

import android.content.Context
import com.example.LC_App.data.remote.api.ApiClient
import com.example.LC_App.data.remote.dto.SensorDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IotRepository (private val context: Context) {

    private val api = ApiClient.getAuthenticatedIotApi(context)

    // Función que emite valores cada cierto intervalo
    fun getSensorStream(intervalMs: Long): Flow<Result<SensorDto>> = flow {
        while (true) {
            try {
                // Intentamos obtener datos reales del backend
                val response = api.getSensorData()
                if (response.isSuccessful && response.body() != null) {
                    emit(Result.success(response.body()!!))
                } else {
                    // Fallback: Si el backend no responde o no tiene la ruta, 
                    // simulamos datos para que la UI funcione y puedas probar la lógica.
                    // Esto es útil si tu backend aún no tiene el endpoint /iot/sensors
                    val simulatedData = simulateSensorData()
                    emit(Result.success(simulatedData))
                }
            } catch (e: Exception) {
                // En caso de error de red (ej: backend apagado), también simulamos para probar UI
                // En producción, aquí emitirías Result.failure(e)
                val simulatedData = simulateSensorData()
                emit(Result.success(simulatedData))
            }
            delay(intervalMs)
        }
    }

    private fun simulateSensorData(): SensorDto {
        // Simula temperatura entre 15 y 30, humedad entre 30 y 80
        val temp = (15..30).random() + Math.random()
        val hum = (30..80).random() + Math.random()
        return SensorDto(temp, hum)
    }
}