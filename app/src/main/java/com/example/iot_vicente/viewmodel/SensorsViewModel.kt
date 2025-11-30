package com.example.iot_vicente.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot_vicente.data.remote.dto.SensorDto
import com.example.iot_vicente.data.repository.IotRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SensorsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = IotRepository()
    
    // Estado de sensores (Temperatura/Humedad)
    private val _sensorData = MutableStateFlow<SensorDto?>(null)
    val sensorData: StateFlow<SensorDto?> = _sensorData

    // Estado de Ampolleta (UI)
    private val _bulbOn = MutableStateFlow(false)
    val bulbOn: StateFlow<Boolean> = _bulbOn

    // Estado de Linterna (Hardware)
    private val _flashlightOn = MutableStateFlow(false)
    val flashlightOn: StateFlow<Boolean> = _flashlightOn
    
    private var sensorJob: Job? = null
    private val cameraManager by lazy {
        application.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    init {
        startSensorStream()
    }

    private fun startSensorStream() {
        sensorJob?.cancel()
        sensorJob = viewModelScope.launch {
            // Intervalo de 2 segundos como pide el ejemplo
            repo.getSensorStream(2000L).collectLatest { result ->
                result.onSuccess { 
                    _sensorData.value = it 
                }
            }
        }
    }

    // --- AMPOLLETA (Solo UI) ---
    fun toggleBulb() {
        _bulbOn.value = !_bulbOn.value
    }

    // --- LINTERNA (Hardware) ---
    fun toggleFlashlight() {
        val newState = !_flashlightOn.value
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                    cameraManager.getCameraCharacteristics(id)
                        .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                }
                
                if (cameraId != null) {
                    cameraManager.setTorchMode(cameraId, newState)
                    _flashlightOn.value = newState
                }
            } else {
                // Manejo para versiones antiguas si fuera necesario (deprecated Camera API)
                // Por ahora asumimos minSdk >= 24 (Android 7) que soporta setTorchMode
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla (ej: sin permisos o sin flash), no cambiamos el estado visual o mostramos error
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Apagar linterna al salir si se desea, o dejarla prendida.
        // Generalmente es buena práctica apagarla si la app muere, 
        // pero aquí el ViewModel sobrevive a rotaciones.
        if (_flashlightOn.value) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val cameraId = cameraManager.cameraIdList.firstOrNull()
                    if (cameraId != null) {
                        cameraManager.setTorchMode(cameraId, false)
                    }
                }
            } catch (e: Exception) { }
        }
    }
}