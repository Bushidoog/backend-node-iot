package com.example.LC_App.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.LC_App.R

data class Developer(
    val name: String,
    val role: String,
    val email: String,
    val institution: String,
    val github: String,
    val linkedin: String
)

@Composable
fun DeveloperScreen(nav: NavController) {
    val developers = listOf(
        Developer(
            name = "Lautaro Oyarzun",
            role = "Full Stack Developer",
            email = "lautaro.oyarzun@inacapmail.cl",
            institution = "INACAP - Analista Programador",
            github = "https://github.com/lautarooyarzun",
            linkedin = "https://linkedin.com/in/lautarooyarzun"
        ),
        Developer(
            name = "Claudio Pérez",
            role = "Backend / Cloud Architect",
            email = "claudio.perez@inacapmail.cl",
            institution = "INACAP - Analista Programador",
            github = "https://github.com/claudioperez",
            linkedin = "https://linkedin.com/in/claudioperez"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Equipo de Desarrollo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Importante para que ocupe el espacio restante
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(developers) { dev ->
                DeveloperCard(dev)
            }
        }
    }
}

@Composable
fun DeveloperCard(dev: Developer) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) // Asegurar fondo blanco para contraste
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar / Imagen (Placeholder)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Avatar de ${dev.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray) // Fondo gris para el avatar si la imagen es transparente
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = dev.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = dev.role, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = dev.institution, fontSize = 12.sp, color = Color.DarkGray)
                Text(text = dev.email, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row {
                    Text(
                        text = "GitHub",
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { openUrl(dev.github) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "LinkedIn",
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { openUrl(dev.linkedin) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeveloperScreenPreview() {
    DeveloperScreen(nav = rememberNavController())
}