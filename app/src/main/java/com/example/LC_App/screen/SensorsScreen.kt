package com.example.LC_App.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.viewmodel.SensorsViewModel
import java.util.Locale
import com.example.LC_App.R

@Composable
fun SensorsScreen(
    nav: NavController,
    vm: SensorsViewModel = viewModel()
) {
    val sensorData by vm.sensorData.collectAsState()
    val bulbOn by vm.bulbOn.collectAsState()
    val flashlightOn by vm.flashlightOn.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título y Logo
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Cambiar por logo AC
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Text("DATOS SENSORES", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)

        Spacer(Modifier.height(30.dp))

        // --- SECCIÓN SENSORES ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Humedad
            SensorItem(
                // Asegúrate de tener iconos en drawable, si no, usa placeholders
                iconRes = R.drawable.ic_launcher_foreground, // R.drawable.ic_humidity
                label = "HUMEDAD",
                value = sensorData?.let { String.format(Locale.US, "%.0f%%", it.humidity) } ?: "--%"
            )

            // Temperatura
            val temp = sensorData?.temperature ?: 0.0
            // Icono dinámico
            // val tempIcon = if (temp > 20) R.drawable.ic_temp_high else R.drawable.ic_temp_low
            val tempIcon = R.drawable.ic_launcher_foreground // Placeholder
            
            SensorItem(
                iconRes = tempIcon,
                label = "TEMPERATURA",
                value = sensorData?.let { String.format(Locale.US, "%.0f°", it.temperature) } ?: "--°",
                valueColor = if (temp > 20) Color(0xFFE65100) else Color(0xFF0277BD) // Naranja vs Azul
            )
        }

        Spacer(Modifier.height(40.dp))

        // --- SECCIÓN ACTUADORES ---
        
        // Ampolleta (UI)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { vm.toggleBulb() }
                .padding(10.dp)
        ) {
            // Icono ampolleta on/off
            // val bulbIcon = if (bulbOn) R.drawable.ic_bulb_on else R.drawable.ic_bulb_off
            val bulbIcon = R.drawable.ic_launcher_foreground
            
            Image(
                painter = painterResource(id = bulbIcon),
                contentDescription = "Ampolleta",
                modifier = Modifier.size(64.dp),
                alpha = if (bulbOn) 1f else 0.3f // Efecto visual simple si no hay iconos distintos
            )
            Spacer(Modifier.width(20.dp))
            Text(
                text = if (bulbOn) "Ampolleta encendida" else "Ampolleta apagada",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(20.dp))

        // Linterna (Hardware)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { vm.toggleFlashlight() }
                .padding(10.dp)
        ) {
            // Icono linterna
            // val flashIcon = R.drawable.ic_flashlight
            val flashIcon = R.drawable.ic_launcher_foreground
            
            Image(
                painter = painterResource(id = flashIcon),
                contentDescription = "Linterna",
                modifier = Modifier.size(64.dp),
                alpha = if (flashlightOn) 1f else 0.3f
            )
            Spacer(Modifier.width(20.dp))
            Text(
                text = if (flashlightOn) "Linterna activada" else "Linterna desactivada",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SensorItem(iconRes: Int, label: String, value: String, valueColor: Color = Color.Blue) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Preview(showBackground = true)
@Composable
fun SensorsScreenPreview() {
    // Mock ViewModel requiere inyección manual o mocking complejo, 
    // mejor usar preview estático extrayendo contenido si fuera necesario.
    // Aquí solo instanciamos para ver layout básico (puede fallar por context de VM)
    // SensorsScreen(nav = rememberNavController())
}