package com.example.LC_App.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.data.remote.dto.UserDto
import com.example.LC_App.viewmodel.UserViewModel
import com.example.LC_App.nav.Route

@Composable
fun UserListScreen(
    nav: NavController,
    vm: UserViewModel = viewModel()
) {
    val users by vm.users.collectAsState()
    val loading by vm.isLoading.collectAsState()
    val error by vm.errorMessage.collectAsState()

    var searchText by remember { mutableStateOf("") }

    // Estados para el diálogo de eliminación
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<UserDto?>(null) }

    // Cargar usuarios al inicio
    LaunchedEffect(Unit) {
        vm.fetchUsers()
    }

    // Filtrado
    val filteredUsers = users.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                (it.email.contains(searchText, ignoreCase = true))
    }

    // --- DIÁLOGO DE CONFIRMACIÓN ---
    if (showDeleteDialog && userToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${userToDelete?.name}?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)), // Rojo
                    onClick = {
                        userToDelete?.let { vm.deleteUser(it.id) }
                        showDeleteDialog = false
                        userToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // --- UI PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fondo blanco limpio
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // <--- ESTO CENTRA LOS ELEMENTOS
    ) {

        // Título agregado
        Text(
            text = "Lista de Usuarios",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        // Buscador
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar usuario") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { vm.fetchUsers() }) {
                    Text("Reintentar")
                }
            }
        } else {
            if (filteredUsers.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay usuarios encontrados", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(filteredUsers) { user ->
                        UserItem(
                            user = user,
                            onEdit = {
                                nav.navigate(Route.EditUser.createRoute(user.id))
                            },
                            onDelete = {
                                userToDelete = user
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: UserDto, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)) // Fondo gris suave
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // --- AQUÍ QUITAMOS EL NULL ---
                // Si surname es null, pone "" (vacío). El trim() quita espacios sobrantes.
                // Verifica si en tu DTO se llama 'surname' o 'last_name'
                val fullName = "${user.name} ${user.last_name ?: ""}".trim()

                Text(text = fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFB00020))
            }
        }
    }
}