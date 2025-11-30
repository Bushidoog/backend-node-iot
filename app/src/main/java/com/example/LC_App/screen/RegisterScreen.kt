package com.example.LC_App.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.LC_App.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.LC_App.ui.theme.BluePrimary
import com.example.LC_App.ui.theme.InputBackground
import com.example.LC_App.ui.theme.TextBlack
import com.example.LC_App.ui.theme.TextDarkGray

@Composable
fun RegisterScreen(
    nav: NavController,
    vm: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") } // Se mantiene en UI por si se habilita a futuro, pero no se envía
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var confirmPwd by remember { mutableStateOf("") }

    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun passwordValidationError(pwd: String): String? {
        val errors = mutableListOf<String>()

        if (pwd.length < 8) errors.add("al menos 8 caracteres")
        if (!pwd.any { it.isUpperCase() }) errors.add("una mayúscula")
        if (!pwd.any { it.isLowerCase() }) errors.add("una minúscula")
        if (!pwd.any { it.isDigit() }) errors.add("un número")
        if (!pwd.any { !it.isLetterOrDigit() }) errors.add("un carácter especial")

        return if (errors.isEmpty()) null
        else "La contraseña debe incluir: ${errors.joinToString(", ")}."
    }

    fun onRegister() {
        message = null

        // 1) Campos vacíos (Phone opcional para backend, pero validamos si queremos que sea obligatorio en UI)
        if (
            name.isBlank() ||
            surname.isBlank() ||
            email.isBlank() ||
            pwd.isBlank() ||
            confirmPwd.isBlank()
        ) {
            message = "Campos obligatorios vacíos."
            isError = true
            return
        }

        // 2) Formato email
        if (!isValidEmail(email)) {
            message = "Formato de e-mail inválido."
            isError = true
            return
        }

        // 3) Contraseñas coinciden
        if (pwd != confirmPwd) {
            message = "Las contraseñas no coinciden."
            isError = true
            return
        }

        // 4) Contraseña robusta
        val pwdError = passwordValidationError(pwd)
        if (pwdError != null) {
            message = "Contraseña débil: $pwdError"
            isError = true
            return
        }

        // 5) Llamar al ViewModel
        loading = true

        // NOTA: 'phone' no se envía porque el backend no lo recibe en /register
        vm.register(
            name = name,
            surname = surname,
            email = email,
            pass = pwd,
            onSuccess = {
                loading = false
                // Enviar mensaje al Login usando SavedStateHandle
                nav.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("registerMessage", "Registro exitoso.")

                // Volver a Login (popBackStack porque vinimos desde ahí)
                nav.popBackStack()
            },
            onFail = { msg ->
                loading = false
                message = msg ?: "Error al registrar."
                isError = true
            }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta", fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
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

            shape = RoundedCornerShape(8.dp) // Bordes un poco más redondeados
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Apellido") },
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

            shape = RoundedCornerShape(8.dp) // Bordes un poco más redondeados
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono (Opcional/No se guarda)") },
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

            shape = RoundedCornerShape(8.dp), // Bordes un poco más redondeados
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
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

        OutlinedTextField(
            value = pwd,
            onValueChange = { pwd = it },
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
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPwd,
            onValueChange = { confirmPwd = it },
            label = { Text("Confirmar contraseña") },
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

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            onClick = { onRegister() }
        ) {
            Text(if (loading) "Registrando..." else "Crear cuenta")
        }

        message?.let {
            Spacer(Modifier.height(10.dp))
            Text(
                text = it,
                color = if (isError) Color.Red else Color(0xFF1B5E20)
            )
        }
    }
}

