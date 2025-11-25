package com.example.iot_vicente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
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
        // Nota: launchWhenCreated está deprecado en versiones recientes de Lifecycle, 
        // pero se puede usar lifecycleScope.launch directamente o repeatOnLifecycle.
        // Usaremos lifecycleScope.launchWhenCreated como pediste, o su equivalente moderno si fuera necesario.
        // Como launchWhenCreated puede marcarse deprecado, aquí uso launch para mayor compatibilidad futura,
        // pero mantengo la lógica.
        lifecycleScope.launchWhenCreated {
            // Aquí podrías leer token, preferencias, etc.
            delay(1200L)
            keepSplash = false
        }

        // 3) Contenido Compose
        setContent {
            Iot_vicenteTheme {
                AppNavGraph() // Tu NavHost con rutas Splash->Home
            }
        }
    }
}