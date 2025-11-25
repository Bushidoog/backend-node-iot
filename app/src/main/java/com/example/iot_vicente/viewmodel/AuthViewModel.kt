package com.example.iot_vicente.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iot_vicente.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    fun register(
        name: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            val res = repo.register(name, email, pass)

            res.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFail(it.message ?: "Error al registrar") }
            )
        }
    }
}


