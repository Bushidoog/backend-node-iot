package com.example.LC_App.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.LC_App.R
import com.example.LC_App.screen.HomeScreen
import com.example.LC_App.screen.LoginScreen
import com.example.LC_App.screen.RecoverPasswordScreen
import com.example.LC_App.screen.RegisterScreen
import com.example.LC_App.screen.SensorsScreen
import com.example.LC_App.screen.UserListScreen
import com.example.LC_App.screen.UserMenuScreen
import com.example.LC_App.viewmodel.AuthViewModel
import com.example.LC_App.viewmodel.SensorsViewModel
import com.example.LC_App.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun AppNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        // Empezamos en Splash, que luego redirigirá a Login
        startDestination = "splash"
    ) {

        // SPLASH
        composable("splash") {
            SplashLottie {
                navController.navigate(Route.Login.path) {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        // LOGIN
        composable(Route.Login.path) {
            LoginScreen(nav = navController)
        }

        // REGISTRO
        composable(Route.Register.path) {
            // Obtiene el ViewModel (versión simple sin Hilt)
            val authVm: AuthViewModel = viewModel()
            RegisterScreen(
                nav = navController,
                vm = authVm
            )
        }

        // RECUPERAR CONTRASEÑA
        composable(Route.Recover.path) {
            val authVm: AuthViewModel = viewModel()
            RecoverPasswordScreen(nav = navController, vm = authVm)
        }

        // HOME / MENÚ PRINCIPAL
        composable(Route.Home.path) {
            HomeScreen(nav = navController)
        }

        // MENÚ GESTIÓN DE USUARIOS
        composable(Route.UserMenu.path) {
             UserMenuScreen(nav = navController)
        }

        // LISTAR USUARIOS (CRUD)
        composable(Route.UserList.path) {
            val userVm: UserViewModel = viewModel()
            UserListScreen(nav = navController, vm = userVm)
        }

        // DATOS DE SENSORES
        composable(Route.Sensors.path) {
            // Se necesita instanciar el VM para manejar lógica de ampolleta/linterna y datos
            val sensorsVm: SensorsViewModel = viewModel()
            SensorsScreen(nav = navController, vm = sensorsVm)
        }

        // DATOS DEL DESARROLLADOR
        composable(Route.Developer.path) {
            // DeveloperScreen(nav = navController)
        }
    }
}

@Composable
fun SplashLottie(onFinish: () -> Unit) {
    // Carga el json desde res/raw/loading.json -> R.raw.loading
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val animState = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // Simula carga (ajusta el tiempo según la duración que quieras)
    LaunchedEffect(composition) {
        // Si no hay composition aún, espera un poco; cuando exista, espera 2.5s y termina
        delay(2500L)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (composition == null) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        } else {
            LottieAnimation(
                composition = composition,
                progress = { animState.progress },
                modifier = Modifier.size(250.dp)
            )
        }
    }
}
