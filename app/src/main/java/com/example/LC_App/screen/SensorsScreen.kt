package com.example.LC_App.screen

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.R
import com.example.LC_App.viewmodel.SensorsViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SensorsScreen(
    nav: NavController,
    vm: SensorsViewModel = viewModel()
) {
    // --- 1. SIMULACIÓN DE API (Polling cada 2 seg) ---
    var humidity by remember { mutableStateOf(30) }
    var temperature by remember { mutableStateOf(18) }

    LaunchedEffect(Unit) {
        while (true) {
            // Simulamos datos aleatorios (Aquí iría tu llamada a la API real)
            humidity = Random.nextInt(20, 60)
            temperature = Random.nextInt(15, 30)
            delay(2000L) // Intervalo configurable (2 segundos)
        }
    }

    // --- 2. LÓGICA DE ICONO DINÁMICO ---
    // Si temp > 20 usa icono rojo/alto, si no, azul/bajo
    val tempIcon = if (temperature > 20) R.drawable.ic_temperature_high else R.drawable.ic_temperature_low
    val tempColor = if (temperature > 20) Color(0xFFFF6D00) else Color(0xFF2979FF)

    // Estados de Actuadores
    var isLightOn by remember { mutableStateOf(false) }
    var isFlashOn by remember { mutableStateOf(false) }

    // Contexto para la linterna real
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.lyclogo),
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

        // --- FILA DE SENSORES ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SensorCard(
                title = "HUMEDAD",
                value = "$humidity%",
                iconRes = R.drawable.ic_humidity,
                valueColor = Color.Blue
            )

            SensorCard(
                title = "TEMPERATURA",
                value = "$temperature°",
                iconRes = tempIcon, // <--- Icono dinámico aquí
                valueColor = tempColor // Color dinámico aquí
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- ACTUADORES ---

        // Ampolleta (Solo UI)
        ActuatorRow(
            title = if (isLightOn) "Ampolleta encendida" else "Ampolleta apagada",
            isOn = isLightOn,
            iconRes = if (isLightOn) R.drawable.ic_light_on else R.drawable.ic_light_off,
            onSwitchChange = { isLightOn = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Linterna (Hardware Real)
        ActuatorRow(
            title = if (isFlashOn) "Linterna activada" else "Linterna desactivada",
            isOn = isFlashOn,
            iconRes = if (isFlashOn) R.drawable.ic_flashlight_on else R.drawable.ic_flashlight_off,
            onSwitchChange = {
                isFlashOn = it
                toggleFlashlight(context, isFlashOn) // <--- Función mágica
            }
        )
    }
}

// --- FUNCIÓN PARA ACTIVAR LINTERNA REAL ---
fun toggleFlashlight(context: Context, state: Boolean) {
    try {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0] // Cámara trasera
        cameraManager.setTorchMode(cameraId, state)
    } catch (e: Exception) {
        e.printStackTrace() // Manejo de error si no tiene flash
    }
}

// (Tus componentes SensorCard y ActuatorRow se mantienen igual...)
@Composable
fun SensorCard(title: String, value: String, iconRes: Int, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            modifier = Modifier.size(110.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun ActuatorRow(title: String, isOn: Boolean, iconRes: Int, onSwitchChange: (Boolean) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Switch(
                checked = isOn,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
            )
        }
    }
}