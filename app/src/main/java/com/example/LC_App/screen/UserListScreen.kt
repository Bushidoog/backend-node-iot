package com.example.LC_App.screen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.data.remote.dto.UserDto
import com.example.LC_App.viewmodel.UserViewModel
import androidx.compose.ui.graphics.Color
import com.example.LC_App.nav.Route

@Composable
fun UserListScreen(
    nav: NavController,
    vm: UserViewModel = viewModel()
) {
    // IMPORTANTE: Usamos 'users' en lugar de 'userList' si ese era el problema.
    // Verificamos cómo se llama en tu ViewModel.
    val users by vm.users.collectAsState()
    val loading by vm.isLoading.collectAsState()
    val error by vm.errorMessage.collectAsState()
    
    var searchText by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<UserDto?>(null) }

    // Cargar usuarios al inicio
    LaunchedEffect(Unit) {
        vm.fetchUsers()
    }

    val filteredUsers = users.filter {
        it.name.contains(searchText, ignoreCase = true) ||
        it.email.contains(searchText, ignoreCase = true)
    }
    
    if (showDeleteDialog && userToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${userToDelete?.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        userToDelete?.let { vm.deleteUser(it.id) }
                        showDeleteDialog = false
                        userToDelete = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Buscador
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar usuario") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                 CircularProgressIndicator()
            }
        } else if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
            Button(onClick = { vm.fetchUsers() }) {
                Text("Reintentar")
            }
        } else {
            LazyColumn {
                items(filteredUsers) { user ->
                    UserItem(
                        user = user,
                        onEdit = { 
                             // Navegar a la pantalla de edición pasando el ID
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

@Composable
fun UserItem(user: UserDto, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${user.name} ${user.last_name}", style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}