package com.example.LC_App.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.LC_App.data.repository.UserRepository
import com.example.LC_App.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application.applicationContext)

    // Lista de usuarios (para la pantalla de lista)
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users = _users.asStateFlow()

    // --- NUEVO: Usuario individual (para la pantalla de edición) ---
    private val _user = MutableStateFlow<UserDto?>(null)
    val user = _user.asStateFlow() // <--- Observaremos esto en EditUserScreen

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        // Opcional: Si quieres cargar la lista al iniciar, descomenta esto.
        // Pero para editar, no es necesario cargar toda la lista.
        fetchUsers()
    }

    // Cargar TODA la lista
    fun fetchUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getUsers()

            result.onSuccess { userList ->
                _users.value = userList
            }.onFailure { error ->
                _errorMessage.value = "Error al obtener usuarios: ${error.message}"
            }

            _isLoading.value = false
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteUser(userId)
                .onSuccess { fetchUsers() }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    // --- MÉTODOS PARA EDICIÓN DE USUARIO ---

    // 1. CARGAR USUARIO INDIVIDUAL DESDE LA API (Nuevo)
    fun loadUserById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Limpiar errores previos

            // Llamamos al repositorio (Asegúrate de tener esta función en UserRepository)
            val result = repository.getUserById(id)

            result.onSuccess { dto ->
                _user.value = dto // ¡Aquí guardamos el usuario que llegó de AWS!
            }.onFailure { error ->
                _errorMessage.value = "Error al cargar usuario: ${error.message}"
            }

            _isLoading.value = false
        }
    }

    // 2. ACTUALIZAR USUARIO (PUT)
    // Nota: Agregué el parámetro 'id' explícito para mayor claridad
    fun updateUser(id: Int, userDto: UserDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            // Llamamos al repositorio
            repository.updateUser(id, userDto)
                .onSuccess {
                    fetchUsers() // Actualizamos la lista por si volvemos atrás
                    onSuccess()  // Avisamos a la pantalla que terminó bien
                }
                .onFailure { error ->
                    _errorMessage.value = "Error al actualizar: ${error.message}"
                }

            _isLoading.value = false
        }
    }
}