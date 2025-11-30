package com.example.LC_App.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.LC_App.R
import com.example.LC_App.nav.Route

@Composable
fun UserMenuScreen(nav: NavController) {
    
    var message by remember { mutableStateOf<String?>(null) }

    // 🔹 Leer mensaje de éxito si volvemos del registro
    val currentBackStackEntry = nav.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val registerMsg = savedStateHandle?.get<String>("registerMessage")
    
    if (registerMsg != null) {
        message = registerMsg
        savedStateHandle.remove<String>("registerMessage")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo / Imagen
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo Gestión",
            modifier = Modifier.height(100.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "GESTIÓN DE USUARIOS",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(Modifier.height(30.dp))

        // Botón Ingresar Usuarios
        Button(
            onClick = { nav.navigate(Route.Register.path) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("INGRESAR USUARIOS", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))

        // Botón Listar Usuarios
        Button(
            onClick = { nav.navigate(Route.UserList.path) }, 
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF37474F))
        ) {
            Text("LISTAR USUARIOS", fontWeight = FontWeight.Bold)
        }

        // Mensaje de éxito (si venimos de registrar usuario)
        message?.let {
            Spacer(Modifier.height(20.dp))
            Text(
                text = it, // "Registro exitoso"
                color = Color(0xFF1B5E20), // Verde oscuro
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserMenuScreenPreview() {
    UserMenuScreen(nav = rememberNavController())
}