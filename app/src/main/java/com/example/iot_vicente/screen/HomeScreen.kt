package com.example.iot_vicente.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iot_vicente.viewmodel.AuthState
import com.example.iot_vicente.viewmodel.AuthViewModel

@Composable
fun HomeContent(
    authState: AuthState,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (authState is AuthState.Authenticated) {
            Text("Bienvenido ${authState.user.name}")
            Spacer(Modifier.height(20.dp))
        }
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun HomeScreen(
    vm: AuthViewModel = viewModel(),
    onLogoutDone: () -> Unit = {}
) {
    val authState by vm.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            onLogoutDone()
        }
    }

    HomeContent(
        authState = authState,
        onLogout = { vm.logout() }
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeContent(
        authState = AuthState.Authenticated(
            user = com.example.iot_vicente.data.remote.dto.UserDto(
                id = 1,
                name = "Lautaro",
                email = "lauta@example.com"
            )
        ),
        onLogout = {}
    )
}
