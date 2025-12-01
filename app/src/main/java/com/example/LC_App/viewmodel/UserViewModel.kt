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

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        fetchUsers()
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

    // --- MÉTODOS PARA EDICIÓN DE USUARIO ---

    // Obtener usuario por ID desde la lista local
    fun getUserById(userId: Int): UserDto? {
        return _users.value.find { it.id == userId }
    }

    // Actualizar usuario en el servidor
    fun updateUser(user: UserDto, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateUser(user)
                .onSuccess {
                    // Actualizar la lista local o recargar
                    fetchUsers() 
                    onSuccess()
                }
                .onFailure {
                    onError("Error al actualizar: ${it.message}")
                }
            _isLoading.value = false
        }
    }
}