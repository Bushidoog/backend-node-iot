package com.example.LC_App.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel // <--- Importante
import androidx.lifecycle.viewModelScope
import com.example.LC_App.data.repository.UserRepository
import com.example.LC_App.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Cambiamos ViewModel() por AndroidViewModel(application)
class UserViewModel(application: Application) : AndroidViewModel(application) {

    // 2. Pasamos el contexto de la aplicación al repositorio
    private val repository = UserRepository(application.applicationContext)

    // Estados para la UI (Cargando, Lista, Error)
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        fetchUsers() // Cargar usuarios automáticamente al iniciar
    }

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
    
    // Podríamos agregar updateUser aquí si implementamos la UI de edición
}