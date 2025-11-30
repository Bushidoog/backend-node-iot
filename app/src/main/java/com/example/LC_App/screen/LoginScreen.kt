package com.example.LC_App.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.LC_App.R
import com.example.LC_App.nav.Route
import com.example.LC_App.ui.theme.BluePrimary
import com.example.LC_App.ui.theme.InputBackground
import com.example.LC_App.ui.theme.LC_AppTheme
import com.example.LC_App.ui.theme.TextBlack
import com.example.LC_App.ui.theme.TextDarkGray
import com.example.LC_App.viewmodel.AuthViewModel

// ---------- UI PRINCIPAL ----------

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
        verticalArrangement = Arrangement.Center
    ) {

        // Logo
        Image(
            painter = painterResource(id = R.drawable.lyclogo),
            contentDescription = "Logo",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Bienvenido",
            fontSize = 23.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        // Usuario / Email
        OutlinedTextField(
            value = user,
            onValueChange = onUserChange,
            label = { Text("Usuario / Email") },
            // ---  VISUAL DE TEXTO  ---
            colors = OutlinedTextFieldDefaults.colors(
                // Texto ingresado (Negro fuerte)
                focusedTextColor = TextBlack,
                unfocusedTextColor = TextBlack,

                // Bordes (Azul cuando escribes, Gris Oscuro cuando no)
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = TextDarkGray,

                // Etiqueta (Label) "Correo..."
                focusedLabelColor = BluePrimary,
                unfocusedLabelColor = TextDarkGray,

                // Fondo del campo (Blanco puro para contraste)
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(Modifier.height(8.dp))

        // Contraseña
        OutlinedTextField(
            value = pass,
            onValueChange = onPassChange,
            label = { Text("Contraseña") },
            // ---  VISUAL DE TEXTO  ---
            colors = OutlinedTextFieldDefaults.colors(
                // Texto ingresado (Negro fuerte)
                focusedTextColor = TextBlack,
                unfocusedTextColor = TextBlack,

                // Bordes (Azul cuando escribes, Gris Oscuro cuando no)
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = TextDarkGray,

                // Etiqueta (Label) "Correo..."
                focusedLabelColor = BluePrimary,
                unfocusedLabelColor = TextDarkGray,

                // Fondo del campo (Blanco puro para contraste)
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(16.dp))

        // Botón Ingresar
        Button(
            onClick = onLoginClick,
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loading) "Cargando..." else "Ingresar")
        }

        Spacer(Modifier.height(8.dp))

        // Recuperar contraseña
        TextButton(
            onClick = onRecoverClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("¿Olvidaste tu contraseña?")
        }

        // Ir a registro
        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }

        // Mensajes (error / éxito)
        message?.let {
            Spacer(Modifier.height(12.dp))
            Text(
                text = it,
                color = if (isError) Color.Red else Color(0xFF1B5E20), // verde oscuro para éxito
                fontSize = 14.sp
            )
        }
    }
}

// ---------- LÓGICA DE LOGIN ----------

@Composable
fun LoginScreen(nav: NavController, vm: AuthViewModel = viewModel()) {

    var user by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    // 🔹 Leer mensaje que viene desde RegisterScreen
    val currentBackStackEntry = nav.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val registerMsg = savedStateHandle?.get<String>("registerMessage")
    if (registerMsg != null) {
        message = registerMsg
        isError = false          // es mensaje de éxito
        savedStateHandle.remove<String>("registerMessage")
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun doLogin() {
        message = null

        // 1) Validar campos vacíos
        if (user.isBlank() || pass.isBlank()) {
            message = "Campos obligatorios vacíos."
            isError = true
            return
        }

        // 2) Validar formato de email
        if (!isValidEmail(user)) {
            message = "Formato de e-mail inválido."
            isError = true
            return
        }

        // 3) Simular usuario bloqueado
        if (user.equals("blocked@demo.cl", ignoreCase = true)) {
            message = "Usuario bloqueado."
            isError = true
            return
        }

        // 4) Llamada a ViewModel
        loading = true
        vm.login(
            email = user,
            pass = pass,
            onSuccess = {
                loading = false
                message = "Login correcto."
                isError = false
                // Navegar al Home inmediatamente
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
             // Navegar a pantalla recuperar (crear ruta si no existe)
             nav.navigate("recover")
        }
    )
}

// ---------- PREVIEW ----------

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
    LC_AppTheme() {
        LoginContent(
            user = "demo@demo.cl",
            pass = "Demo123*",
            message = "Ejemplo de mensaje de error",
            isError = true,
            loading = false,
            onUserChange = {},
            onPassChange = {},
            onLoginClick = {},
            onRegisterClick = {},
            onRecoverClick = {}
        )
    }
}