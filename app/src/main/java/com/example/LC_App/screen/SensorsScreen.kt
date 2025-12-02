package com.example.LC_App.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.R // <--- IMPORTANTE: Tu R
import com.example.LC_App.viewmodel.SensorsViewModel

@Composable
fun SensorsScreen(
    nav: NavController,
    vm: SensorsViewModel = viewModel()
) {
    // Observamos los datos del ViewModel (simulados o reales)
    // Asumiendo que tienes estos estados en tu VM. Si no, usa datos falsos por ahora.
    val humidity = "31%"
    val temperature = "27°"
    var isLightOn by remember { mutableStateOf(false) } // Estado local para probar switch
    var isFlashOn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding() // Respetar la cámara
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 1. LOGO SUPERIOR (Opcional, como en tus otras pantallas)
        Image(
            painter = painterResource(id = R.drawable.lyclogo), // Tu logo
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "DATOS SENSORES",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 2. FILA DE SENSORES (Humedad y Temperatura)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Tarjeta Humedad
            SensorCard(
                title = "HUMEDAD",
                value = humidity,
                iconRes = R.drawable.ic_humidity, // <--- TU IMAGEN AQUÍ
                valueColor = Color.Blue
            )

            // Tarjeta Temperatura
            SensorCard(
                title = "TEMPERATURA",
                value = temperature,
                iconRes = R.drawable.ic_temperature, // <--- TU IMAGEN AQUÍ
                valueColor = Color(0xFFFF6D00) // Naranja
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 3. ACTUADORES (Lista vertical)

        // Ampolleta
        ActuatorRow(
            title = if (isLightOn) "Ampolleta encendida" else "Ampolleta apagada",
            isOn = isLightOn,
            // Cambia el icono según si está prendida o apagada
            iconRes = if (isLightOn) R.drawable.ic_light_on else R.drawable.ic_light_off,
            onSwitchChange = { isLightOn = it } // Aquí llamarías a vm.toggleLight()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Linterna
        ActuatorRow(
            title = if (isFlashOn) "Linterna activada" else "Linterna desactivada",
            isOn = isFlashOn,
            iconRes = R.drawable.ic_flashlight, // Tu imagen de linterna
            onSwitchChange = { isFlashOn = it }
        )
    }
}

// --- COMPONENTE 1: TARJETA DE SENSOR (Cuadrada) ---
@Composable
fun SensorCard(
    title: String,
    value: String,
    iconRes: Int,
    valueColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            modifier = Modifier.size(110.dp) // Tamaño cuadrado
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(50.dp), // Tamaño del icono
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

// --- COMPONENTE 2: FILA DE ACTUADOR (Interruptor) ---
@Composable
fun ActuatorRow(
    title: String,
    isOn: Boolean,
    iconRes: Int,
    onSwitchChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icono
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                // Texto
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Interruptor (Switch)
            Switch(
                checked = isOn,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}