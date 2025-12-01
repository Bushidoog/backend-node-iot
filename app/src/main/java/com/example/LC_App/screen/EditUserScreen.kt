package com.example.LC_App.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.LC_App.data.remote.dto.UserDto
import com.example.LC_App.viewmodel.UserViewModel

@Composable
fun EditUserScreen(
    nav: NavController,
    vm: UserViewModel,
    userId: Int
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cargar datos del usuario al iniciar
    LaunchedEffect(userId) {
        val user = vm.getUserById(userId)
        if (user != null) {
            name = user.name
            lastName = user.last_name
            email = user.email
            isLoading = false
        } else {
            errorMessage = "Usuario no encontrado"
            isLoading = false
        }
    }

    fun onSave() {
        if (name.isBlank() || lastName.isBlank() || email.isBlank()) {
            errorMessage = "Todos los campos son obligatorios"
            return
        }
        
        vm.updateUser(
            UserDto(id = userId, name = name, last_name = lastName, email = email),
            onSuccess = {
                nav.popBackStack() // Volver a la lista
            },
            onError = { msg ->
                errorMessage = msg
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Editar Usuario", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { onSave() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
            
            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = Color.Red)
            }
        }
    }
}