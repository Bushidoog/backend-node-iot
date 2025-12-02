package com.example.LC_App.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.LC_App.R

@Composable
fun DeveloperScreen(nav: NavController) {
    // Color de fondo gris claro similar al de la imagen
    val backgroundColor = Color(0xFFF5F7F8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding() // Respetar la zona de la cámara
    ) {
        // TÍTULO SUPERIOR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Equipo de Desarrollo",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1) // Un azul oscuro para el título
            )
        }

        // CONTENEDOR PRINCIPAL CENTRADO
        // Usamos Box con Alignment.Center para centrar verticalmente las tarjetas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f), // Ocupa el espacio restante
            contentAlignment = Alignment.Center // Centra el contenido vertical y horizontalmente
        ) {
            // Columna scrolleable por si las tarjetas son muy altas en pantallas pequeñas
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- TARJETA 1: Lautaro ---
                DeveloperCard(
                    imageRes = R.drawable.foto_lautaro, // <--- ASEGÚRATE DE TENER ESTA IMAGEN
                    name = "Lautaro Oyarzun",
                    role = "Full Stack Developer",
                    institution = "INACAP - Analista Programador",
                    email = "lautaro.oyarzun@inacapmail.cl",
                    githubUrl = "https://github.com/", // Pon aquí su URL real
                    linkedinUrl = "https://linkedin.com/" // Pon aquí su URL real
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- TARJETA 2: Claudio ---
                DeveloperCard(
                    imageRes = R.drawable.foto_claudio, // <--- ASEGÚRATE DE TENER ESTA IMAGEN
                    name = "Claudio Pérez",
                    role = "Backend / Cloud Architect",
                    institution = "INACAP - Analista Programador",
                    email = "claudio.perez@inacapmail.cl",
                    githubUrl = "https://github.com/", // Pon aquí tu URL real
                    linkedinUrl = "https://linkedin.com/" // Pon aquí tu URL real
                )

                // Espacio extra abajo para que no quede pegado al borde si se scrollea
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- COMPONENTE REUTILIZABLE PARA LA TARJETA DEL DESARROLLADOR ---
@Composable
fun DeveloperCard(
    imageRes: Int,
    name: String,
    role: String,
    institution: String,
    email: String,
    githubUrl: String,
    linkedinUrl: String
) {
    // Handler para abrir enlaces en el navegador
    val uriHandler = LocalUriHandler.current
    val linkColor = Color(0xFF1565C0) // Azul para enlaces

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. LA IMAGEN CIRCULAR
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Foto de $name",
                modifier = Modifier
                    .size(90.dp) // Tamaño de la imagen
                    .clip(CircleShape), // Recorte circular
                contentScale = ContentScale.Crop // Recorta la imagen para llenar el círculo
            )

            Spacer(modifier = Modifier.width(20.dp))

            // 2. LOS DATOS DE TEXTO
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = role, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = institution, fontSize = 12.sp, color = Color.DarkGray)

                // Email clickable
                Text(
                    text = email,
                    fontSize = 12.sp,
                    color = linkColor,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("mailto:$email")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // FILA DE ENLACES (GitHub y LinkedIn)
                Row {
                    Text(
                        text = "GitHub",
                        color = linkColor,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { uriHandler.openUri(githubUrl) }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = "LinkedIn",
                        color = linkColor,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { uriHandler.openUri(linkedinUrl) }
                    )
                }
            }
        }
    }
}