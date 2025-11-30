package com.example.LC_App.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.LC_App.data.remote.dto.UserDto
import com.example.LC_App.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repo: UserRepository = UserRepository()
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadUsers() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = repo.getUsers()
            result.onSuccess {
                _users.value = it
            }.onFailure {
                _error.value = it.message
            }
            _loading.value = false
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            repo.deleteUser(userId)
                .onSuccess { loadUsers() }
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }
    
    // Podríamos agregar updateUser aquí si implementamos la UI de edición
}