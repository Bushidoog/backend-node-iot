package com.example.iot_vicente.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iot_vicente.R
import com.example.iot_vicente.screen.HomeScreen
import com.example.iot_vicente.screen.LoginScreen
import com.example.iot_vicente.screen.RegisterScreen
import kotlinx.coroutines.delay


// Lottie imports (añadir si vas a usar Lottie)
import com.airbnb.lottie.compose.*
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") {
            // Usa la variante Lottie si tienes el json en res/raw/loading.json
            SplashLottie {
                nav.navigate(Route.Login.path) {
                    popUpTo("splash") { inclusive = true }
                }
            }
            // Si prefieres el static splash, reemplaza por:
            // SplashScreen { ... }
        }
        composable(Route.Login.path) { LoginScreen(nav) }
        composable(Route.Register.path) { RegisterScreen(nav) }
        composable(Route.Home.path) { HomeScreen() }
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    // Composable minimal (logo centrado y fondo de marca)
    LaunchedEffect(Unit) {
        // fallback visual corto por seguridad
        delay(250L)
        onFinish()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            // usa drawable si tienes ic_launcher_foreground; mipmap puede funcionar pero drawable es preferible
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.Center),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SplashLottie(onFinish: () -> Unit) {
    // carga el json desde res/raw/loading.json -> R.raw.loading
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val animState = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // Simula carga (ajusta el tiempo según la duración que quieras)
    LaunchedEffect(composition) {
        // Si no hay composition aún, espera un poco; cuando exista, espera 1.5s y termina
        delay(1500L)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        if (composition == null) {
            CircularProgressIndicator()
        } else {
            LottieAnimation(
                composition = composition,
                progress = { animState.progress },
                modifier = Modifier.size(220.dp)
            )
        }
    }
}
