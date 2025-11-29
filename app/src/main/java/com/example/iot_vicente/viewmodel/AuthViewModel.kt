package com.example.iot_vicente.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot_vicente.data.remote.dto.RegisterResponse
import com.example.iot_vicente.data.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    // --- LOGIN ---
    fun login(email: String, pass: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.login(email, pass)
                if (response.success) onSuccess()
                else onFail(response.message)
            } catch (e: Exception) {
                onFail(handleError(e))
            }
        }
    }

    // --- REGISTRO ---
    fun register(
        name: String,
        surname: String,
        // 'phone' eliminado ya que el backend no lo soporta
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Pasamos solo lo que el repo y la API esperan
                val response = repo.register(name, surname, email, pass)
                
                // Usamos la propiedad success que definimos en el DTO (verifica error == null)
                if (response.success) onSuccess()
                else onFail(response.displayMessage)
            } catch (e: Exception) {
                onFail(handleError(e))
            }
        }
    }

    // --- RECUPERAR CONTRASEÑA 1: Enviar código ---
    fun forgotPassword(email: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.forgotPassword(email)
                if (response.success) onSuccess()
                else onFail(response.message)
            } catch (e: Exception) {
                onFail(handleError(e))
            }
        }
    }

    // --- RECUPERAR CONTRASEÑA 2: Resetear con código ---
    fun resetPassword(email: String, code: String, newPass: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.resetPassword(email, code, newPass)
                if (response.success) onSuccess()
                else onFail(response.message)
            } catch (e: Exception) {
                onFail(handleError(e))
            }
        }
    }

    private fun handleError(e: Exception): String {
        return if (e is HttpException) {
            try {
                val errorBody = e.response()?.errorBody()?.string()
                if (!errorBody.isNullOrEmpty()) {
                    // Ahora RegisterResponse tiene campo 'error' y 'displayMessage'
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    errorResponse.displayMessage
                } else {
                    "Error del servidor: ${e.code()}"
                }
            } catch (e2: Exception) {
                "Error parseando respuesta: ${e.code()}"
            }
        } else {
            e.message ?: "Error de conexión o desconocido"
        }
    }
}