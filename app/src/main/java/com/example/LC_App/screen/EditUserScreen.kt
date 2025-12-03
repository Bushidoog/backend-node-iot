package com.example.LC_App.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Importante para el tamaño de fuente
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.data.remote.dto.UserDto
import com.example.LC_App.viewmodel.UserViewModel

@Composable
fun EditUserScreen(
    nav: NavController,
    userId: Int,
    vm: UserViewModel = viewModel()
) {
    val userState by vm.user.collectAsState()
    val loading by vm.isLoading.collectAsState()
    val error by vm.errorMessage.collectAsState()

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId != 0) vm.loadUserById(userId)
    }

    LaunchedEffect(userState) {
        userState?.let { user ->
            name = user.name
            lastName = user.last_name ?: ""
            email = user.email
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Título en Negro fuerte
        Text(
            text = "Editar Usuario",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black // <--- Forzamos negro
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    // --- ARREGLO DE TEXTO CLARO ---
                    focusedTextColor = Color.Black,   // Texto al escribir
                    unfocusedTextColor = Color.Black, // Texto al salir
                    cursorColor = Color.Black,        // Cursor
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.DarkGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Apellido
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    // --- ARREGLO DE TEXTO CLARO ---
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.DarkGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email (Bloqueado)
            OutlinedTextField(
                value = email,
                onValueChange = { },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color(0xFFE0E0E0),
                    disabledTextColor = Color.DarkGray, // Un gris oscuro legible
                    disabledBorderColor = Color.LightGray,
                    disabledLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (error != null) {
                Text(text = error ?: "", color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    val updatedUser = UserDto(
                        id = userId,
                        name = name,
                        last_name = lastName,
                        email = email
                    )
                    vm.updateUser(userId, updatedUser) {
                        nav.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}