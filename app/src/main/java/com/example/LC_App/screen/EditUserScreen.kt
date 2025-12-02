package com.example.LC_App.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.viewmodel.UserViewModel
import com.example.LC_App.data.remote.dto.UserDto

@Composable
fun EditUserScreen(
    nav: NavController,
    userId: Int,
    vm: UserViewModel = viewModel()
) {
    // 1. ESTADO: Observamos la variable 'user' del ViewModel
    // Cuando el ViewModel termine de descargar, esta variable se actualizará sola.
    val userState by vm.user.collectAsState()
    val loading by vm.isLoading.collectAsState()
    val error by vm.errorMessage.collectAsState()

    // 2. FORMULARIO: Variables locales para los campos de texto
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") } // Usamos lastName para evitar confusión
    var email by remember { mutableStateOf("") }

    // 3. CARGAR: Al entrar, le pedimos al ViewModel que busque el usuario
    LaunchedEffect(userId) {
        if (userId != 0) {
            vm.loadUserById(userId)
        }
    }

    // 4. RELLENAR: Cuando 'userState' cambie (lleguen los datos), llenamos el formulario
    LaunchedEffect(userState) {
        userState?.let { user ->
            name = user.name
            lastName = user.last_name ?: "" // Arreglo del null
            email = user.email
        }
    }

    // --- UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fondo blanco
            .statusBarsPadding() // <--- ¡ESTO ARREGLA LA ALTURA!
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Editar Usuario", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
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
                enabled = false // Generalmente el email no se edita, o puedes dejarlo true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // MENSAJE DE ERROR (Si hubo problema al cargar)
            if (error != null) {
                Text(text = error ?: "", color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    // Preparamos el objeto con los datos nuevos
                    // Nota: el ID y Password se mantienen o se manejan aparte
                    val updatedUser = UserDto(
                        id = userId,
                        name = name,
                        last_name = lastName,
                        email = email
                    )

                    // Llamamos a actualizar
                    vm.updateUser(userId, updatedUser) {
                        // Si sale bien (onSuccess), volvemos atrás
                        nav.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}