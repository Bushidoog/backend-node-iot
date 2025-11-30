package com.example.LC_App

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.LC_App.nav.AppNavGraph
import com.example.LC_App.ui.theme.LC_AppTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    private var keepSplash = true // condición para mantener el splash visible

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1) Instalar Splash antes de super.onCreate
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { keepSplash }

        super.onCreate(savedInstanceState)

        // 2) Simular/realizar inicialización breve (1–2 s)
        // Mantiene el splash nativo visible un poco más de tiempo
        // Aunque ahora AppNavGraph tiene su propio splash, esto ayuda a que la carga inicial sea suave.
        lifecycleScope.launchWhenCreated {
            // Aquí podrías leer token, preferencias, etc.
            delay(1000L)
            keepSplash = false
        }

        // 3) Contenido Compose
        setContent {
            LC_AppTheme() {
                // Creamos el NavController aquí para pasarlo al AppNavGraph
                val navController = rememberNavController()
                AppNavGraph(navController = navController) 
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // <--- AQUÍ FORZAMOS EL BLANCO
        contentAlignment = Alignment.Center
    ) {
        // Aquí va tu animación oscura (Lottie, CircularProgressIndicator, etc.)
        // Ejemplo:
        CircularProgressIndicator(color = Color.Black)
    }
}