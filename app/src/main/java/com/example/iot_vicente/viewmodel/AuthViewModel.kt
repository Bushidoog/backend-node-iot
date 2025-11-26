package com.example.iot_vicente.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot_vicente.data.repository.AuthRepository
import com.example.iot_vicente.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    data object Checking : AuthState()           // Splash chequeando
    data object Unauthenticated : AuthState()    // Ir a Login
    data class Authenticated(val user: UserDto) : AuthState() // Ir a Home
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repo = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Checking)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkSession()
    }

    private fun appContext() = getApplication<Application>().applicationContext

    fun checkSession() {
        viewModelScope.launch {
            val ctx = appContext()

            val token = repo.getStoredToken(ctx)
            if (token.isNullOrEmpty()) {
                _authState.value = AuthState.Unauthenticated
                return@launch
            }

            val res = repo.validateToken(ctx)
            _authState.value = res.fold(
                onSuccess = { user -> AuthState.Authenticated(user) },
                onFailure = { AuthState.Unauthenticated }
            )
        }
    }

    fun login(email: String, pass: String) {
        _authState.value = AuthState.Checking
        viewModelScope.launch {
            val ctx = appContext()
            val res = repo.login(ctx, email, pass)

            _authState.value = res.fold(
                onSuccess = { AuthState.Authenticated(it.user) },
                onFailure = {
                    AuthState.Error(it.message ?: "Error al iniciar sesión")
                }
            )
        }
    }

    fun register(
        name: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Nota: El repositorio actual solo acepta name, email, password.
            // Los campos surname y phone se están ignorando por ahora hasta que se actualice el DTO/API.
            val res = repo.register(name, email, pass)
            res.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFail(it.message ?: "Error al registrar") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.clearToken(appContext())
            _authState.value = AuthState.Unauthenticated
        }
    }
}
