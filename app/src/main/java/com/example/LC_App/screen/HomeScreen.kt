package com.example.LC_App.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.LC_App.R // <--- IMPORTANTE: Asegúrate que este import sea correcto
import com.example.LC_App.nav.Route
import com.example.LC_App.ui.theme.LC_AppTheme
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O) // Necesario para la hora exacta
@Composable
fun HomeScreen(nav: NavController) {

    // --- LÓGICA DEL RELOJ (Hora Chile) ---
    var currentDateTime by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(Unit) {
        while (true) {
            // Usamos ZonedDateTime para forzar la zona horaria de Chile
            val zdt = ZonedDateTime.now(ZoneId.of("America/Santiago"))
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            currentDateTime = zdt.format(formatter)
            delay(1000L)
        }
    }

    // --- UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        // Usamos SpaceEvenly para distribuir mejor el espacio verticalmente
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        // 1. LOGO (Agregado aquí)
        Image(
            painter = painterResource(id = R.drawable.lyclogo), // Asegúrate de tener la imagen
            contentDescription = "Logo Smart IoT",
            modifier = Modifier.size(150.dp)
        )

        // 2. TÍTULO
        Text(
            text = "Menú Principal",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // 3. TARJETA DE HORA
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)) // Un gris muy suave
        ) {
            Text(
                text = "Fecha y hora: $currentDateTime",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        Spacer(Modifier.height(16.dp))

        // 4. BOTONES (Agrupados)
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botón CRUD de Usuarios
            MenuButtonMain(text = "CRUD de Usuarios") {
                nav.navigate(Route.UserMenu.path)
            }

            // Botón Datos de Sensores
            MenuButtonMain(text = "Ver datos de sensores") {
                nav.navigate(Route.Sensors.path)
            }

            // Botón Datos del desarrollador
            MenuButtonMain(text = "Datos del desarrollador") {
                nav.navigate(Route.Developer.path)
            }
        }

        // Espacio final para equilibrio
        Spacer(Modifier.height(16.dp))
    }
}

// Componente auxiliar para que los botones se vean iguales y bonitos
@Composable
fun MenuButtonMain(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(12.dp) // Bordes redondeados modernos
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

// Preview
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    LC_AppTheme {
        HomeScreen(nav = rememberNavController())
    }
}