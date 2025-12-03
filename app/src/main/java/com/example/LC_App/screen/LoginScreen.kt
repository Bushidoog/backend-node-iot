package com.example.LC_App.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.R
import com.example.LC_App.nav.Route
import com.example.LC_App.viewmodel.AuthViewModel
// Asegúrate de que estos imports de tu tema sean correctos
// Si te marca error en los colores, usa MaterialTheme.colorScheme.primary
import com.example.LC_App.ui.theme.BluePrimary
import com.example.LC_App.ui.theme.InputBackground
import com.example.LC_App.ui.theme.TextBlack
import com.example.LC_App.ui.theme.TextDarkGray

// ---------- UI PRINCIPAL (DISEÑO) ----------

@Composable
fun LoginContent(
    user: String,
    pass: String,
    message: String?,
    isError: Boolean,
    loading: Boolean,
    onUserChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo
        Image(
            painter = painterResource(id = R.drawable.lyclogo),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Bienvenido",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // Campo Usuario / Email
        OutlinedTextField(
            value = user,
            onValueChange = onUserChange,
            label = { Text("Usuario / Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground,
                focusedTextColor = TextBlack,
                unfocusedTextColor = TextBlack,
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = TextDarkGray
            )
        )

        Spacer(Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = pass,
            onValueChange = onPassChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground,
                focusedTextColor = TextBlack,
                unfocusedTextColor = TextBlack,
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = TextDarkGray
            )
        )

        Spacer(Modifier.height(24.dp))

        // --- BOTÓN INGRESAR (CON ANIMACIÓN) ---
        Button(
            onClick = onLoginClick,
            enabled = !loading, // Deshabilita click si carga
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BluePrimary,
                disabledContainerColor = BluePrimary.copy(alpha = 0.7f)
            )
        ) {
            if (loading) {
                // Ruedita de carga blanca
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text("Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Enlaces de Recuperar y Registro
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onRecoverClick) {
                Text("¿Olvidaste tu contraseña?", fontSize = 12.sp, color = BluePrimary)
            }
            TextButton(onClick = onRegisterClick) {
                Text("Regístrate", fontSize = 12.sp, color = BluePrimary)
            }
        }

        // Mensaje de Error / Éxito
        if (message != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = message,
                color = if (isError) Color.Red else Color(0xFF1B5E20),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

// ---------- LÓGICA DE NEGOCIO (VIEWMODEL) ----------

@Composable
fun LoginScreen(nav: NavController, vm: AuthViewModel = viewModel()) {

    var user by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    // 🔹 Leer mensajes de otras pantallas (Registro o Recuperación)
    val currentBackStackEntry = nav.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val registerMsg = savedStateHandle?.get<String>("registerMessage")
    val recoverMsg = savedStateHandle?.get<String>("loginMessage")

    // Efecto para capturar mensajes al volver
    LaunchedEffect(registerMsg, recoverMsg) {
        if (registerMsg != null) {
            message = registerMsg
            isError = false
            savedStateHandle?.remove<String>("registerMessage")
        } else if (recoverMsg != null) {
            message = recoverMsg
            isError = false
            savedStateHandle?.remove<String>("loginMessage")
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun doLogin() {
        message = null

        // 1. Validaciones Locales
        if (user.isBlank() || pass.isBlank()) {
            message = "Campos obligatorios vacíos."
            isError = true
            return
        }
        if (!isValidEmail(user)) {
            message = "Formato de e-mail inválido."
            isError = true
            return
        }
        // Simulación usuario bloqueado
        if (user.equals("blocked@demo.cl", ignoreCase = true)) {
            message = "Usuario bloqueado."
            isError = true
            return
        }

        // 2. Llamada al Backend
        loading = true
        vm.login(
            email = user,
            pass = pass,
            onSuccess = {
                loading = false
                message = "Login correcto."
                isError = false
                // Navegar al Home
                nav.navigate(Route.Home.path) {
                    popUpTo(Route.Login.path) { inclusive = true }
                }
            },
            onFail = { errorMsg ->
                loading = false
                message = errorMsg ?: "Credenciales inválidas."
                isError = true
            }
        )
    }

    // Renderizar UI
    LoginContent(
        user = user,
        pass = pass,
        message = message,
        isError = isError,
        loading = loading,
        onUserChange = { user = it },
        onPassChange = { pass = it },
        onLoginClick = { doLogin() },
        onRegisterClick = { nav.navigate(Route.Register.path) },
        onRecoverClick = {
            // Asegúrate de tener esta ruta en tu Route.kt
            nav.navigate("recover_password")
        }
    )
}