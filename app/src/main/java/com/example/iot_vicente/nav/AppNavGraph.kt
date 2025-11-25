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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iot_vicente.R
import com.example.iot_vicente.screen.HomeScreen
import com.example.iot_vicente.screen.LoginScreen
import com.example.iot_vicente.screen.RegisterScreen
// Asumo que tu ViewModel está en este paquete, ajusta si es necesario
import com.example.iot_vicente.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

// Lottie imports
import com.airbnb.lottie.compose.*
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    // Instanciamos el ViewModel aquí para que esté disponible
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") {
            SplashLottie {
                nav.navigate(Route.Login.path) {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable(Route.Login.path) { LoginScreen(nav) }

        // Ahora 'authViewModel' ya existe y se pasa correctamente
        composable(Route.Register.path) { RegisterScreen(nav, authViewModel) }

        composable(Route.Home.path) { HomeScreen() }
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(250L)
        onFinish()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
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
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val animState = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(composition) {
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
