package com.example.LC_App.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.LC_App.data.remote.dto.RegisterResponse
import com.example.LC_App.data.repository.AuthRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

// CAMBIO 1: Usamos AndroidViewModel para tener acceso al contexto (application)
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // CAMBIO 2: Inicializamos el repositorio pasando el contexto
    private val repo = AuthRepository(application.applicationContext)

    // --- LOGIN ---
    fun login(email: String, pass: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // El repo ahora devuelve Response<LoginResponse>
                val response = repo.login(email, pass)

                if (response.isSuccessful) {
                    val body = response.body()
                    // Verificamos si el body existe y si el backend dice success=true
                    // Nota: Ajusta 'body?.token' si tu lógica depende de la existencia del token
                    if (body != null) {
                        onSuccess()
                    } else {
                        onFail("Respuesta vacía del servidor")
                    }
                } else {
                    // Si el código es 400, 401, etc.
                    val errorMsg = parseErrorBody(response.errorBody())
                    onFail(errorMsg)
                }
            } catch (e: Exception) {
                onFail("Error de conexión: ${e.message}")
            }
        }
    }

    // --- REGISTRO ---
    fun register(
        name: String,
        surname: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repo.register(name, surname, email, pass)

                if (response.isSuccessful && response.body()?.success == true) {
                    onSuccess()
                } else {
                    // Si el backend envía el error en el cuerpo (status 200 pero success false)
                    // O si es un error HTTP (status 400)
                    val msg = response.body()?.displayMessage
                        ?: parseErrorBody(response.errorBody())
                    onFail(msg)
                }
            } catch (e: Exception) {
                onFail("Error de conexión: ${e.message}")
            }
        }
    }

    // --- RECUPERAR CONTRASEÑA ---
    fun forgotPassword(email: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.forgotPassword(email)
                if (response.isSuccessful && response.body()?.success == true) {
                    onSuccess()
                } else {
                    val msg = response.body()?.message ?: parseErrorBody(response.errorBody())
                    onFail(msg)
                }
            } catch (e: Exception) {
                onFail("Error de conexión: ${e.message}")
            }
        }
    }

    // --- RESET PASSWORD ---
    fun resetPassword(email: String, code: String, newPass: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.resetPassword(email, code, newPass)
                if (response.isSuccessful && response.body()?.success == true) {
                    onSuccess()
                } else {
                    val msg = response.body()?.message ?: parseErrorBody(response.errorBody())
                    onFail(msg)
                }
            } catch (e: Exception) {
                onFail("Error de conexión: ${e.message}")
            }
        }
    }

    // Función auxiliar para leer el error que manda Node.js cuando el status no es 200
    private fun parseErrorBody(errorBody: ResponseBody?): String {
        return try {
            val jsonString = errorBody?.string()
            if (!jsonString.isNullOrEmpty()) {
                // Intentamos leer el campo "message" o "displayMessage" del JSON de error
                val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)

                if (jsonObject.has("message")) jsonObject.get("message").asString
                else if (jsonObject.has("displayMessage")) jsonObject.get("displayMessage").asString
                else "Error del servidor"
            } else {
                "Error desconocido"
            }
        } catch (e: Exception) {
            "Error al leer respuesta"
        }
    }
}