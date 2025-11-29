package com.example.iot_vicente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.iot_vicente.nav.AppNavGraph
import com.example.iot_vicente.ui.theme.Iot_vicenteTheme
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
            Iot_vicenteTheme {
                // Creamos el NavController aquí para pasarlo al AppNavGraph
                val navController = rememberNavController()
                AppNavGraph(navController = navController) 
            }
        }
    }
}