package com.example.iot_vicente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.iot_vicente.nav.AppNavGraph
import com.example.iot_vicente.ui.theme.Iot_vicenteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar Splash API de Android 12+ (necesario para inicializar correctamente el tema)
        installSplashScreen()
        
        super.onCreate(savedInstanceState)

        setContent {
            Iot_vicenteTheme {
                // La navegación inicia en 'splash' que muestra la animación Lottie
                AppNavGraph()
            }
        }
    }
}
