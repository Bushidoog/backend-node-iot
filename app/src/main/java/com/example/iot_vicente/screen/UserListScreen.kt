package com.example.iot_vicente.screen

import androidx.compose.foundation.clickable
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
import com.example.iot_vicente.data.remote.dto.UserDto
import com.example.iot_vicente.viewmodel.UserViewModel

@Composable
fun UserListScreen(
    nav: NavController,
    vm: UserViewModel = viewModel()
) {
    val users by vm.users.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    
    var searchText by remember { mutableStateOf("") }

    // Cargar usuarios al inicio
    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    val filteredUsers = users.filter {
        it.name.contains(searchText, ignoreCase = true) ||
        it.email.contains(searchText, ignoreCase = true)
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
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
            Button(onClick = { vm.loadUsers() }) {
                Text("Reintentar")
            }
        } else {
            LazyColumn {
                items(filteredUsers) { user ->
                    UserItem(
                        user = user,
                        onEdit = { /* TODO: Navegar a pantalla de edición */ },
                        onDelete = { vm.deleteUser(user.id) }
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