package com.example.iot_vicente.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.iot_vicente.nav.Route
import com.example.iot_vicente.ui.theme.Iot_vicenteTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(nav: NavController) {

    var currentDateTime by rememberSaveable { mutableStateOf(getCurrentDateTimeString()) }

    // Actualizar hora cada 1 segundo
    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = getCurrentDateTimeString()
            delay(1000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Menú Principal",
            fontSize = 24.sp
        )

        Spacer(Modifier.height(12.dp))

        // Fecha y hora en tiempo real
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Fecha y hora: $currentDateTime",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(24.dp))

        // Botón CRUD de Usuarios
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { nav.navigate(Route.UserMenu.path) } // menú gestión usuarios
        ) {
            Text("CRUD de Usuarios")
        }

        Spacer(Modifier.height(12.dp))

        // Botón Datos de Sensores
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { nav.navigate(Route.Sensors.path) }
        ) {
            Text("Ver datos de sensores")
        }

        Spacer(Modifier.height(12.dp))

        // Botón Datos del desarrollador
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { nav.navigate(Route.Developer.path) }
        ) {
            Text("Datos del desarrollador")
        }
    }
}

// Función para obtener fecha/hora en formato requerido
private fun getCurrentDateTimeString(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

// Preview
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Iot_vicenteTheme {
        HomeScreen(nav = rememberNavController())
    }
}
