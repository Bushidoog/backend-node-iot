package com.example.LC_App.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color // <--- Esta era la importación que faltaba
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. Configuramos el Modo Oscuro (usando tus nuevos colores)
private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = ErrorRed,
    background = Color(0xFF121212), // Fondo oscuro
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// 2. Configuramos el Modo Claro (El que queremos mejorar)
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = ErrorRed,

    // Fondos claros y legibles
    background = BackgroundWhite,
    surface = Color.White,

    // Textos oscuros para alto contraste
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextBlack,
    onSurface = TextBlack,
    onTertiary = Color.White
)

@Composable
fun LC_AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color está en true por defecto en Android 12+
    // Puedes ponerlo en 'false' si quieres forzar SIEMPRE tus colores azules
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Aquí pintamos la barra de estado (donde está la hora y batería)
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de que Typography exista en Type.kt
        content = content
    )
}